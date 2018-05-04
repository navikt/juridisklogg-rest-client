package no.nav.common.juridisklogg.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result.Success
import com.github.kittinunf.result.Result.Failure
import org.slf4j.LoggerFactory
import java.lang.Exception

class RestArchiver(var username: String, var password: String, var url: String) {

    companion object {
        @JvmStatic private val logger = LoggerFactory.getLogger(RestArchiver::class.java)
        @JvmStatic val mapper = jacksonObjectMapper()
    }

    /**
     * Archives the document to Legal Archive by sending a POST request with the given ArchiveRequest instance,
     * returning the ID of the archive as a String if successful.
     * The method will throw an exception if the request resulted in an error
     * @param   request     an instance of ArchiveRequest - the body to be sent
     * @return              the ID of the archive if the operation was successful
     * @throws  Exception   if the request resulted in an error response from the endpoint
     */

    @Throws(Exception::class)
    fun archiveDocument(archiveRequest: ArchiveRequest) : String {
        val (request, response, result) = Fuel.post(url)
            .header("Content-Type" to "application/json")
            .body(mapper.writeValueAsString(archiveRequest))
            .authenticate(username, password)
            .response()

        logger.debug("Request: {}:", request.toString())
        logger.debug("Response: {}", response.toString())

        when (result) {
            is Failure -> {
                throw result.getException().exception
            }
            is Success -> {
                return mapper.readTree(result.get()).get("id").asText()
            }
        }
    }
}
