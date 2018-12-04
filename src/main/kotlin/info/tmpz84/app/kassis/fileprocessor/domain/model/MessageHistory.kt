package info.tmpz84.app.kassis.fileprocessor.domain.model

data class MessageHistory(
        var id: Int? = null,

        val msgid: String,
        val row: String,
        val message_type: String = "",
        val status: String  = "",
        val note: String,
        val note2: String
        )