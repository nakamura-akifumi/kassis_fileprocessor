package info.tmpz84.app.kassis.fileprocessor.domain.data

data class KassisFileMessage(val msgid:String,
                             val filepath:String,
                             val message_type:String,
                             val blob: MessageBlob?)