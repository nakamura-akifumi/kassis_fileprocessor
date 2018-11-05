package info.tmpz84.app.kassis.fileprocessor

import info.tmpz84.app.kassis.fileprocessor.doma.dao.UserDao
import info.tmpz84.app.kassis.fileprocessor.domain.DaoFactory
import org.springframework.test.context.ContextConfiguration
import info.tmpz84.app.kassis.fileprocessor.domain.data.MessageAdapter
import info.tmpz84.app.kassis.fileprocessor.domain.model.KassisFileMessage
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ContextConfiguration(classes = [(TestApplicationContext::class)])
class ParseFileStringSpecTest(private val service: ParseFile) : StringSpec() {

    override fun listeners() = listOf(SpringListener)

    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, this)
        println("Before every test suite")

        val dao = DaoFactory.create(UserDao::class)
        dao.deleteWithoutAdmin()
    }

    init {
        println("test start")

        val dao = DaoFactory.create(UserDao::class)
        dao.selectCount() shouldBe 1

        val homedir = System.getProperty("user.home")
        val xlsxpath = "${homedir}/IdeaProjects/kassis_soda/storage/rh/44/rh44YD9zgNEDzPnjNd58srB6"

        "executeでparamが1の場合oneが返る" {
            val b: MessageAdapter = MessageAdapter(
                    "rh44YD9zgNEDzPnjNd58srB6",
                    "利用者登録2000.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "Xx4KczpeWHwSfFet1vxVvA==")

            val param: KassisFileMessage
                    = KassisFileMessage(
                    "a302dece-ed4f-4af0-901c-938d8e2ed558",
                    "${xlsxpath}",
                    b)

            //service.parse(param) shouldBe "1"
            service.testone() shouldBe "one"
        }
    }
}