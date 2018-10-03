package no.nav.common.juridisklogg.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result.Success
import com.github.kittinunf.result.Result.Failure
import java.lang.Exception
import java.lang.RuntimeException

class RestArchiver(private val username: String, private val password: String, private val url: String) {

    /**
     * Archives the document to Legal Archive by sending a POST request with the given ArchiveRequest instance,
     * returning the ID of the archive as a String if successful.
     * The method will throw an exception if the request resulted in an error
     * @param request an instance of ArchiveRequest - the body to be sent
     * @return the ID of the archive if the operation was successful
     * @throws LegalArchiveException if the request resulted in an error response from the endpoint
     */

    @Throws(Exception::class)
    fun archiveDocument(archiveRequest: ArchiveRequest): String {
        val (_, _, result) = url.httpPost()
            .body(mapper.writeValueAsString(archiveRequest), Charsets.UTF_8)
            .header(mapOf("Content-Type" to "application/json"))
            .authenticate(username, password)
            .responseString()

        return when (result) {
            is Failure -> result.getException().exception.let { throw LegalArchiveException(it.localizedMessage, it) }
            is Success -> mapper.readTree(result.get()).get("id").asText()
        }
    }

    companion object {
        @JvmStatic val mapper = jacksonObjectMapper()
    }
}

class LegalArchiveException(
    override val message: String,
    override val cause: Throwable
) : RuntimeException(message, cause)
