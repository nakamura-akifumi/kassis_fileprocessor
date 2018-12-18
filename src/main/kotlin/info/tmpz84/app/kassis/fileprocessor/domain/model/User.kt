package info.tmpz84.app.kassis.fileprocessor.domain.model

import java.sql.Time
import java.sql.Timestamp

data class User(
        var id: Int? = null,
        var username: String? = null,
        var email: String? = null,
        var personid: String? = null,
        var cardid: String? = null,
        var full_name: String? = "",
        var full_name_transcription: String? = "",
        var note: String? = "",
        var password: String? = "",
        var expired_at: Timestamp? = null,
        var deactive: Boolean = false,
        var deactive_at: Timestamp? = null
        )