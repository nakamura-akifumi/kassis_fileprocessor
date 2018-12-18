package info.tmpz84.app.kassis.fileprocessor.domain.data

data class KassisFileProcessMessage(
        val msgid:String,
        val state:String,
        val msg:String,
        val percent:Float,
        val count:Int,
        val recordSize:Int)