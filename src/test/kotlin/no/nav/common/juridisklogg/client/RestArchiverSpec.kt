package no.nav.common.juridisklogg.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.apache.http.HttpException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class RestArchiverSpec : Spek({

    val server = WireMockServer(options().dynamicPort()).apply {
        start()
    }

    val mapper = jacksonObjectMapper()

    val port = server.port()
    val username = "username"
    val password = "password"
    val wrongPassword = "wrong"
    val url = "http://localhost:$port/archive"

    server.apply {
        stubFor(post(urlEqualTo("/archive"))
                .atPriority(1)
                .withBasicAuth(username, password)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"1\"}")))
        stubFor(post(urlEqualTo("/archive"))
                .atPriority(2)
                .withBasicAuth(username, wrongPassword)
                .willReturn(aResponse()
                        .withStatus(401)
                        .withStatusMessage("Unauthorized")))
    }

    given("a valid request") {
        val request = ArchiveRequest(messageId = "1", messageContent = "hello".toByteArray(), sender = "sender",
                receiver = "receiver")
        given("valid credentials") {
            val archiver = RestArchiver(username, password, url)
            on("archiveDocument") {
                val archiveId = archiver.archiveDocument(request)
                it("should have sent the request to the server") {
                    server.verify(1, postRequestedFor(urlEqualTo("/archive"))
                        .withHeader("Content-Type", equalTo("application/json"))
                        .withBasicAuth(BasicCredentials(username, password))
                        .withRequestBody(equalToJson(mapper.writeValueAsString(request)))
                    )
                }
                it("should return an archiveId") {
                    archiveId shouldEqual "1"
                }
            }
        }
        given("invalid credentials") {
            val archiver = RestArchiver(username, wrongPassword, url)
            on("archiveDocument") {
                val archiveId = { archiver.archiveDocument(request) }
                val expectedErrorMessage = "HTTP Exception 401 Unauthorized"
                it("should throw an exception with error message: $expectedErrorMessage") {
                    archiveId shouldThrow HttpException::class withMessage expectedErrorMessage
                }
                it("should have sent a request to the server") {
                    server.verify(1, postRequestedFor(urlEqualTo("/archive"))
                        .withHeader("Content-Type", equalTo("application/json"))
                        .withBasicAuth(BasicCredentials(username, wrongPassword))
                        .withRequestBody(equalToJson(mapper.writeValueAsString(request)))
                    )
                }
            }
        }
    }

    afterGroup {
        server.stop()
    }
})