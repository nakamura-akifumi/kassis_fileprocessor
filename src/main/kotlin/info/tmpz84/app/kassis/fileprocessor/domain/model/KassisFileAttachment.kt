package info.tmpz84.app.kassis.fileprocessor.domain.model

import java.sql.Timestamp

data class KassisFileAttachment(
        var id: Int? = null,

        val msgid: String,
        val fileid: String = "",
        val filename: String  = "",
        val byte_size: Long = 0L,
        val checksum: String,
        val create_at: Timestamp? = null,
        val exportfullpath: String = ""
        )