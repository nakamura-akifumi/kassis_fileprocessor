package info.tmpz84.app.kassis.fileprocessor

import java.io.File
import java.io.InputStream
import com.monitorjbl.xlsx.StreamingReader;
import com.sun.corba.se.spi.orbutil.threadpool.Work
import info.tmpz84.app.kassis.fileprocessor.doma.dao.UserDao
import info.tmpz84.app.kassis.fileprocessor.doma.repository.UserRepositoryDomaImpl
import info.tmpz84.app.kassis.fileprocessor.domain.DaoFactory
import info.tmpz84.app.kassis.fileprocessor.domain.data.KassisFileMessage
import info.tmpz84.app.kassis.fileprocessor.domain.model.User
import info.tmpz84.app.kassis.fileprocessor.domain.repository.UserRepository
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.FileInputStream

@Service
class ParseFile() {

    fun parse(kassisFileMessage: KassisFileMessage):Int {

        val dao = DaoFactory.create(UserDao::class)
        val userRepository = UserRepositoryDomaImpl(dao)

        val filefullpath:String = kassisFileMessage.filepath

        val inputStream:InputStream = FileInputStream(File(filefullpath))
        val workbook:Workbook = StreamingReader.builder()
                .rowCacheSize(100)   // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)       // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(inputStream)                // InputStream or File for XLSX file (required)


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

                    userRepository.create(u)
                    processLine++
                }

            }
        }

        return processLine

    }

}
