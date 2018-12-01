package info.tmpz84.app.kassis.fileprocessor

import info.tmpz84.app.kassis.fileprocessor.domain.model.KassisFileMessage

interface ParseFile {
    fun parseManager(kassisFileMessage: KassisFileMessage):Int
    fun parseExcelFile(kassisFileMessage: KassisFileMessage):Int
    fun testone():String
}
