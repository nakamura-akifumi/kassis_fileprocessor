package info.tmpz84.app.kassis.fileprocessor

import java.io.File
import java.io.InputStream
import com.monitorjbl.xlsx.StreamingReader;
import info.tmpz84.app.kassis.fileprocessor.doma.dao.UserDao
import info.tmpz84.app.kassis.fileprocessor.doma.repository.UserRepositoryDomaImpl
import info.tmpz84.app.kassis.fileprocessor.domain.DaoFactory
import info.tmpz84.app.kassis.fileprocessor.domain.model.KassisFileMessage
import info.tmpz84.app.kassis.fileprocessor.domain.model.User
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.stereotype.Component
import java.io.FileInputStream


@Component
class ParseFileImpl: ParseFile {

    override fun testone(): String {
        return "one"
    }
    override fun parse(kassisFileMessage: KassisFileMessage):Int {

        val dao = DaoFactory.create(UserDao::class)
        val userRepository = UserRepositoryDomaImpl(dao)

        val filefullpath:String = kassisFileMessage.filepath

        val inputStream:InputStream = FileInputStream(File(filefullpath))
        val workbook:Workbook = StreamingReader.builder()
                .rowCacheSize(100)   // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)       // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(inputStream)                // InputStream or File for XLSX file (required)

        val tm = ConfigAdapter.transactionManager

        var line:Int?
        var index:Int?
        var processLine:Int = 0
        for (sheet:Sheet in workbook) {
            line = 0

            for (r in sheet) {

                line++
                if (line == 1) {
                    //タイトル行
                    index = 0
                    for (c in r) {
                        System.out.println(c.stringCellValue)
                        index++
                    }
                } else {
                    //情報行
                    index = 0
                    var u:User = User()
                    for (c in r) {
                        when (index) {
                            0 -> {

                            }
                            1 -> {
                                u.username = c.stringCellValue
                            }
                            2 -> {
                                u.cardid = c.stringCellValue
                            }
                            3 -> {
                                u.full_name = c.stringCellValue
                            }
                            4 -> {
                                u.full_name_transcription = c.stringCellValue
                            }
                            11 -> {
                                u.email = c.stringCellValue
                            }
                            else -> {
                                println("else:${index}:${c.stringCellValue}")
                            }
                        }

                        index++
                    }

                    tm.required {
                        userRepository.create(u)
                        processLine++
                    }
                }

            }
        }

        return processLine

    }

}
