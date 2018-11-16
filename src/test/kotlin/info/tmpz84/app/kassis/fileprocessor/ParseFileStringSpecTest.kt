package info.tmpz84.app.kassis.fileprocessor

import info.tmpz84.app.kassis.fileprocessor.doma.dao.UserDao
import info.tmpz84.app.kassis.fileprocessor.domain.DaoFactory
import org.springframework.test.context.ContextConfiguration
import info.tmpz84.app.kassis.fileprocessor.domain.data.MessageAdapter
import info.tmpz84.app.kassis.fileprocessor.domain.model.KassisFileMessage
import info.tmpz84.app.kassis.fileprocessor.domain.model.User
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ContextConfiguration(classes = [(TestApplicationContext::class)])
class ParseFileStringSpecTest(private val service: ParseFile) : StringSpec() {

    override fun listeners() = listOf(SpringListener)

    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, this)
        println("@@@0-0 Before every test suite")

        println("@@@1-1: try delete without admin")
        val dao = DaoFactory.create(UserDao::class)
        val tm = ConfigAdapter.transactionManager
        tm.required {
            dao.deleteWithoutAdmin()
        }

        var user: User = User()
        user.username = "usernamefoo"
        user.password = "userpassword"

        println("@@@1-2: finish")
    }

    init {
        println("@@@10-1:test start")

        val dao = DaoFactory.create(UserDao::class)
        val tm = ConfigAdapter.transactionManager
        tm.required {
            val c = dao.selectCount()
            //c shouldBe 1
        }

        val homedir = System.getProperty("user.home")
        val xlsxpath = "${homedir}/IdeaProjects/kassis_soda/ext/sample/利用者登録3.xlsx"


        "executeでparamが1の場合oneが返る" {
            val b: MessageAdapter = MessageAdapter(
                    "rh44YD9zgNEDzPnjNd58srB6",
                    "利用者登録3.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "Xx4KczpeWHwSfFet1vxVvA==")

            val param: KassisFileMessage
                    = KassisFileMessage(
                    "a302dece-ed4f-4af0-901c-938d8e2ed558",
                    "${xlsxpath}",
                    b)

            service.parse(param) shouldBe 200
            //service.testone() shouldBe "one"
        }

    }
}