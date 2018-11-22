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
import org.springframework.boot.SpringBootConfiguration
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.FileInputStream


@SpringBootConfiguration
@Service
class ParseFileImpl: ParseFile {

    override fun testone(): String {
        return "one"
    }

    override fun parse(kassisFileMessage: KassisFileMessage):Int {

        val ldapService = LdapService()
        ldapService.connect()

        val dao = DaoFactory.create(UserDao::class)
        val userRepository = UserRepositoryDomaImpl(dao)

        val filefullpath:String = kassisFileMessage.filepath

        val inputStream:InputStream = FileInputStream(File(filefullpath))
        val workbook:Workbook = StreamingReader.builder()
                .rowCacheSize(100)   // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)       // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(inputStream)                // InputStream or File for XLSX file (required)

        val tm = ConfigAdapter.transactionManager

        var maxcol:Int = 0
        var line:Int?
        var index:Int?
        var processLine:Int = 0
        for (sheet:Sheet in workbook) {
            line = 0

            for (row in sheet) {

                line++
                if (line == 1) {
                    //タイトル行
                    index = 0
                    for ((index, c) in row.withIndex()) {
                        System.out.println("$index ${c.stringCellValue}")
                        maxcol = index
                    }

                } else {

                    //情報行
                    var u = User()
                    index = 0
                    for (index in 0..maxcol) {
                        val cell = row.getCell(index)
                        if (cell == null) {
                            println("empty(null) ${index}")
                            continue
                        }
                        System.out.println("$index ${cell.stringCellValue}")
                        if (cell.stringCellValue.trim() == "" || cell.stringCellValue == null) {
                            println("empty ${index}")
                            continue
                        }
                        when (index) {
                            0 -> {
                                //Person ID
                            }
                            1 -> {
                                u.username = cell.stringCellValue
                                //println("username:${index}:${cell.stringCellValue}")
                            }
                            2 -> {
                                u.cardid = cell.stringCellValue
                            }
                            3 -> {
                                u.full_name = cell.stringCellValue
                                //println("full_name:${index}:${cell.stringCellValue}")
                            }
                            4 -> {
                                u.full_name_transcription = cell.stringCellValue
                            }
                            10 -> {
                                u.password = cell.stringCellValue
                                println("password:${index}:${cell.stringCellValue}")
                            }
                            11 -> {
                                u.email = cell.stringCellValue
                                //println("email:${index}:${cell.stringCellValue}")
                            }
                            else -> {
                                //println("else:${index}:${c.stringCellValue}")
                            }
                        }

                    }

                    tm.required {
                        userRepository.create(u)
                        if (u?.username != null) {
                            println("password=${u.password}")
                            ldapService.create(u)
                        }
                        processLine++
                    }
                }

            }
        }

        ldapService.disconnect()

        return processLine

    }

}
