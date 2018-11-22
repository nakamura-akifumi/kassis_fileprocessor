import java.util.concurrent.CountDownLatch
import org.springframework.stereotype.Component
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import info.tmpz84.app.kassis.fileprocessor.ParseFileImpl
import info.tmpz84.app.kassis.fileprocessor.domain.model.KassisFileMessage

@Component
class Receiver {

    val latch = CountDownLatch(1)

    fun receiveMessage(body: ByteArray) {
        val json = String(body, charset("UTF-8"))
        val mapper = jacksonObjectMapper()
        val kassisFileMessage = mapper.readValue<KassisFileMessage>(json)

        println("Received <$json>")
        println("msgid: ${kassisFileMessage.msgid}")
        println("filepath: ${kassisFileMessage.filepath}")
        println("filename: ${kassisFileMessage.blob.filename}")
        println("content_type: ${kassisFileMessage.blob.content_type}")

        val parseFile = ParseFileImpl()
        parseFile.parse(kassisFileMessage)
        latch.countDown()
    }

}