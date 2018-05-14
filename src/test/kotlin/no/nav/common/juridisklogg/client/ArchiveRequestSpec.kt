package no.nav.common.juridisklogg.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
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

    given("new ArchiveRequests") {
        on("%s",
            data("request with required parameters only",
                ArchiveRequest.Builder().setMessageId(id).setMessageContent(content).setSender(sender)
                    .setReceiver(receiver).build(),
                expected = "{\"meldingsId\":\"$id\",\"avsender\":\"$sender\",\"mottaker\":\"$receiver\",\"meldingsInnhold\":\"$contentEncoded\"}"
            ),
            data("request with required parameters and joark reference",
                ArchiveRequest.Builder().setMessageId(id).setMessageContent(content).setSender(sender)
                    .setReceiver(receiver).setJoarkRef(joarkRef).build(),
                expected = "{\"meldingsId\":\"$id\",\"avsender\":\"$sender\",\"mottaker\":\"$receiver\",\"meldingsInnhold\":\"$contentEncoded\",\"joarkRef\":\"$joarkRef\"}"
            ),
            data("request with required parameters and retention",
                ArchiveRequest.Builder().setMessageId(id).setMessageContent(content).setSender(sender)
                    .setReceiver(receiver).setRetention(retention).build(),
                expected = "{\"meldingsId\":\"$id\",\"avsender\":\"$sender\",\"mottaker\":\"$receiver\",\"meldingsInnhold\":\"$contentEncoded\",\"antallAarLagres\":$retention}"
            ),
            data("request with all parameters",
                ArchiveRequest.Builder().setMessageId(id).setMessageContent(content).setSender(sender)
                        .setReceiver(receiver).setJoarkRef(joarkRef).setRetention(retention).build(),
                expected = "{\"meldingsId\":\"$id\",\"avsender\":\"$sender\",\"mottaker\":\"$receiver\",\"meldingsInnhold\":\"$contentEncoded\",\"joarkRef\":\"$joarkRef\",\"antallAarLagres\":$retention}"
            )
        ) { _, request, expected ->
            it("should correctly serialize to JSON") {
                mapper.writeValueAsString(request) shouldEqual expected
            }
        }
    }
})