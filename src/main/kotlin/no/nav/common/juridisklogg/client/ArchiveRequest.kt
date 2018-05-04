package no.nav.common.juridisklogg.client

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class ArchiveRequest(
    @JsonProperty(value = "meldingsId", required = true) val messageId: String,
    @JsonProperty(value = "avsender", required = true) val sender: String,
    @JsonProperty(value = "mottaker", required = true) val receiver: String,
    @JsonProperty(value = "meldingsInnhold", required = true) var messageContent: ByteArray,
    @JsonProperty(value = "joarkRef") val joarkReference: String? = null,
    @JsonProperty(value = "antallAarLagres") val retentionInYears: Int? = null) {

    // Builder class for "simplified" Java construction
    class Builder {
        private lateinit var messageId: String
        private lateinit var messageContent: ByteArray
        private lateinit var sender: String
        private lateinit var receiver: String
        private var joarkReference: String? = null
        private var retentionInYears: Int? = null

        fun setMessageId(messageId: String): Builder = apply { this.messageId = messageId }
        fun setMessageContent(messageContent: ByteArray): Builder = apply { this.messageContent = messageContent }
        fun setSender(sender: String): Builder = apply { this.sender = sender }
        fun setReceiver(receiver: String): Builder = apply { this.receiver = receiver }
        fun setJoarkRef(joarkReference: String): Builder = apply { this.joarkReference = joarkReference }
        fun setRetention(retentionInYears: Int): Builder = apply { this.retentionInYears = retentionInYears }

        fun build(): ArchiveRequest = ArchiveRequest(
            messageId = messageId,
            sender = sender,
            receiver = receiver,
            messageContent = messageContent,
            retentionInYears = retentionInYears,
            joarkReference = joarkReference
        )
    }
}