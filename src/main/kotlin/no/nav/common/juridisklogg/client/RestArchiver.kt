package no.nav.common.juridisklogg.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result.Success
import com.github.kittinunf.result.Result.Failure
import org.slf4j.LoggerFactory
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
        val (request, response, result) = Fuel.post(url)
            .header("Content-Type" to "application/json")
            .body(mapper.writeValueAsString(archiveRequest))
            .authenticate(username, password)
            .response()

        logger.debug("Request: {}:", request.toString())
        logger.debug("Response: {}", response.toString())

        return when (result) {
            is Failure -> result.getException().exception.let { throw LegalArchiveException(it.localizedMessage, it) }
            is Success -> mapper.readTree(result.get()).get("id").asText()
        }
    }

    companion object {
        @JvmStatic private val logger = LoggerFactory.getLogger(RestArchiver::class.java)
        @JvmStatic val mapper = jacksonObjectMapper()
    }
}

class LegalArchiveException(
    override val message: String,
    override val cause: Throwable
) : RuntimeException(message, cause)
