package no.nav.common.juridisklogg.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.charset.Charset
import java.util.Base64

class ArchiveRequestSpec : Spek({
    val mapper = jacksonObjectMapper()
    val encoder = Base64.getEncoder()
    
    val id = "1"
    val sender = "sender"
    val receiver = "receiver"
    val joarkRef = "ref"
    val retention = 10
    
    val content = "test".toByteArray()
    val contentEncoded = encoder.encode(content).toString(Charset.forName("UTF-8"))

    given("a request with required parameters only") {
        val expected = "{\"meldingsId\":\"$id\",\"avsender\":\"$sender\",\"mottaker\":\"$receiver\",\"meldingsInnhold\":\"$contentEncoded\"}"
        on("new ArchiveRequest") {
            val request = ArchiveRequest(messageId = id, messageContent = content, sender = sender, receiver = receiver)
            it("should correctly serialize to JSON") {
                mapper.writeValueAsString(request) shouldEqual expected
            }
        }
        on("new ArchiveRequestBuilder") {
            val request = ArchiveRequest.Builder()
                    .setMessageId(id)
                    .setMessageContent(content)
                    .setSender(sender)
                    .setReceiver(receiver)
                    .build()
            it("should correctly serialize to JSON") {
                mapper.writeValueAsString(request) shouldEqual expected
            }
        }
    }

    given("a request with required parameters and Joark Reference") {
        val expected = "{\"meldingsId\":\"$id\",\"avsender\":\"$sender\",\"mottaker\":\"$receiver\",\"meldingsInnhold\":\"$contentEncoded\",\"joarkRef\":\"$joarkRef\"}"
        on("new ArchiveRequest") {
            val request = ArchiveRequest(messageId = id, messageContent = content, sender = sender, receiver = receiver,
                    joarkReference = joarkRef)
            it("should correctly serialize to JSON") {
                mapper.writeValueAsString(request) shouldEqual expected
            }
        }
        on("new ArchiveRequestBuilder") {
            val request = ArchiveRequest.Builder()
                    .setMessageId(id)
                    .setMessageContent(content)
                    .setSender(sender)
                    .setReceiver(receiver)
                    .setJoarkRef(joarkRef)
                    .build()
            it("should correctly serialize to JSON") {
                mapper.writeValueAsString(request) shouldEqual expected
            }
        }
    }

    given("a request with required parameters and Retention specified") {
        val expected = "{\"meldingsId\":\"$id\",\"avsender\":\"$sender\",\"mottaker\":\"$receiver\",\"meldingsInnhold\":\"$contentEncoded\",\"antallAarLagres\":$retention}"
        on("new ArchiveRequest") {
            val request = ArchiveRequest(messageId = id, messageContent = content, sender = sender, receiver = receiver,
                    retentionInYears = retention)
            it("should correctly serialize to JSON") {
                mapper.writeValueAsString(request) shouldEqual expected
            }
        }
        on("new ArchiveRequestBuilder") {
            val request = ArchiveRequest.Builder()
                    .setMessageId(id)
                    .setMessageContent(content)
                    .setSender(sender)
                    .setReceiver(receiver)
                    .setRetention(retention)
                    .build()
            it("should correctly serialize to JSON") {
                mapper.writeValueAsString(request) shouldEqual expected
            }
        }
    }

    given("a request with all parameters") {
        val expected = "{\"meldingsId\":\"$id\",\"avsender\":\"$sender\",\"mottaker\":\"$receiver\",\"meldingsInnhold\":\"$contentEncoded\",\"joarkRef\":\"$joarkRef\",\"antallAarLagres\":$retention}"
        on("new ArchiveRequest") {
            val request = ArchiveRequest(messageId = id, messageContent = content, sender = sender, receiver = receiver,
                    joarkReference = joarkRef, retentionInYears = retention)
            it("should correctly serialize to JSON") {
                mapper.writeValueAsString(request) shouldEqual expected
            }
        }
        on("new ArchiveRequestBuilder") {
            val request = ArchiveRequest.Builder()
                    .setMessageId(id)
                    .setMessageContent(content)
                    .setSender(sender)
                    .setReceiver(receiver)
                    .setJoarkRef(joarkRef)
                    .setRetention(retention)
                    .build()
            it("should correctly serialize to JSON") {
                mapper.writeValueAsString(request) shouldEqual expected
            }
        }
    }
})