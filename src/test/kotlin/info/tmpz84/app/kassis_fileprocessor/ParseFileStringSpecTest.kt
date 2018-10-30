package info.tmpz84.app.kassis_fileprocessor

import info.tmpz84.app.kassis.fileprocessor.ParseFile
import info.tmpz84.app.kassis.fileprocessor.domain.data.KassisFileMessage
import info.tmpz84.app.kassis.fileprocessor.domain.data.MessageAdapter
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec


class ParseFileStringSpecTest : StringSpec() {
    init {

        val service = ParseFile()
        val homedir = System.getProperty("user.home")
        val xlsxpath = "${homedir}/IdeaProjects/kassis_soda/storage/rh/44/rh44YD9zgNEDzPnjNd58srB6"

        "executeでparamが1の場合oneが返る" {
            val b:MessageAdapter = MessageAdapter(
                    "rh44YD9zgNEDzPnjNd58srB6",
                    "利用者登録2000.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "Xx4KczpeWHwSfFet1vxVvA==")

            val param:KassisFileMessage
                    = KassisFileMessage(
                    "a302dece-ed4f-4af0-901c-938d8e2ed558",
                    "${xlsxpath}",
                    b)

            service.parse(param) shouldBe "1"
        }

    }
}