package info.tmpz84.app.kassis.fileprocessor.domain.model

import info.tmpz84.app.kassis.fileprocessor.domain.data.MessageAdapter

data class KassisFileMessage(val msgid:String, val filepath:String, val blob: MessageAdapter)