package info.tmpz84.app.kassis.fileprocessor.domain.model

import info.tmpz84.app.kassis.fileprocessor.domain.data.MessageBlob

data class KassisFileMessage(val msgid:String, val filepath:String, val blob: MessageBlob)