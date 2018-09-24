package no.nav.common.juridisklogg.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.auth.basic.BasicAuth
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.runBlocking
import org.apache.http.HttpException
import org.slf4j.LoggerFactory
import java.lang.Exception

class RestArchiver(var username: String, var password: String, var url: String) {

    /**
     * Archives the document to Legal Archive by sending a POST request with the given ArchiveRequest instance,
     * returning the ID of the archive as a String if successful.
     * The method will throw an exception if the request resulted in an error
     * @param request an instance of ArchiveRequest - the body to be sent
     * @return the ID of the archive if the operation was successful
     * @throws Exception if the request resulted in an error response from the endpoint
     */
    val httpClient = HttpClient(Apache) {
        install(BasicAuth) {
            username = this@RestArchiver.username
            password = this@RestArchiver.password
        }
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
    }

    @Throws(Exception::class)
    fun archiveDocument(archiveRequest: ArchiveRequest): String =
        runBlocking(DefaultDispatcher) {
            val response =
                httpClient.post<HttpResponse> {
                    url(this@RestArchiver.url)
                    header(HttpHeaders.Accept, ContentType.Application.Json.toString())
                    contentType(ContentType.Application.Json)
                    body = archiveRequest
                }
            when (response.status.isSuccess()) {
                true -> mapper.readTree(response.readText()).get("id").asText()
                else -> throw HttpException("HTTP Exception ${response.status.value} ${response.status.description}")
            }
        }

    companion object {
        @JvmStatic private val logger = LoggerFactory.getLogger(RestArchiver::class.java)
        @JvmStatic val mapper = jacksonObjectMapper()
    }
}
