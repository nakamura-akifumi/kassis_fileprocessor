package info.tmpz84.app.kassis.fileprocessor

import info.tmpz84.app.kassis.fileprocessor.domain.model.KassisFileMessage

interface ParseFile {
    fun parse(kassisFileMessage: KassisFileMessage):Int
    fun testone():String
}
