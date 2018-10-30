package info.tmpz84.app.kassis.fileprocessor.domain.model

data class User(
        var id: Int? = null,
        var username: String? = null,
        var email: String? = "",
        var personid: String? = null,
        var cardid: String? = "",
        var full_name: String? = "",
        var full_name_transcription: String? = "",
        var note: String? = ""
        )