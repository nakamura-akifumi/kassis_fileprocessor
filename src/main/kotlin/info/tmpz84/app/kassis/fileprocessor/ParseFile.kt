package info.tmpz84.app.kassis.fileprocessor

import info.tmpz84.app.kassis.fileprocessor.domain.data.KassisFileMessage
import info.tmpz84.app.kassis.fileprocessor.domain.model.KassisFileAttachment

interface ParseFile {
    fun parseManager(kassisFileMessage: KassisFileMessage):Int
    fun parseExcelFile(kassisFileMessage: KassisFileMessage):Int

    fun buildManager(kassisFileMessage: KassisFileMessage):Int
    fun buildExcelFile(kassisFileMessage: KassisFileMessage):Pair<Int, KassisFileAttachment>
}
