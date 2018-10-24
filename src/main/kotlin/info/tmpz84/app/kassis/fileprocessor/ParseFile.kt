package info.tmpz84.app.kassis.fileprocessor

import info.tmpz84.app.kassis.fileprocessor.data.KassisFileMessage
import java.io.File
import org.apache.poi.ss.usermodel.WorkbookFactory

class ParseFile {
    fun parse(kassisFileMessage: KassisFileMessage) {

        val filefullpath: String = kassisFileMessage.filepath

        val inputFile = File(filefullpath)
        val inputStream = inputFile.inputStream()
        val workbook = WorkbookFactory.create(inputStream);
        val firstSheet = workbook.getSheetAt(0)

        firstSheet.forEach {
            it.forEach {
                if (it.columnIndex > 0) print(",")
                print(it)
            }
            println()
        }

    }

}
