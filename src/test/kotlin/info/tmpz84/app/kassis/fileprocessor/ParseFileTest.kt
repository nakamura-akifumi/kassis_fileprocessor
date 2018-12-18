package info.tmpz84.app.kassis.fileprocessor

import info.tmpz84.app.kassis.fileprocessor.doma.dao.UserDao
import info.tmpz84.app.kassis.fileprocessor.domain.DaoFactory
import info.tmpz84.app.kassis.fileprocessor.domain.data.MessageBlob
import info.tmpz84.app.kassis.fileprocessor.domain.data.KassisFileMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import java.io.File
import java.sql.Timestamp


@ExtendWith(SpringExtension::class)
@SpringBootTest
class ParseFileTest {

    @Autowired
    private lateinit var service: ParseFile

    // TODO: DIされない...
    lateinit var userDao: UserDao

    init {
        userDao = DaoFactory.create(UserDao::class)
    }

    @BeforeEach
    fun setUp() {
        println("setUp")
        val tm = ConfigAdapter.transactionManager
        tm.required {
            userDao.deleteWithoutAdmin()
        }
    }

    @Test
    fun someCreateTest() {

        val file = File("src/test/resources/利用者登録サンプル.xlsx")
        val xlsxpath = file.getCanonicalPath()

        val timestamp_now = Timestamp(System.currentTimeMillis())

        val b: MessageBlob = MessageBlob(
                "",
                "",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                ""
        )

        val param: KassisFileMessage
                = KassisFileMessage(
                "a302dece-ed4f-4af0-901c-938d8e2ed558",
                "${xlsxpath}",
                b)

        assertEquals(4, service.parseManager(param))

    }
}