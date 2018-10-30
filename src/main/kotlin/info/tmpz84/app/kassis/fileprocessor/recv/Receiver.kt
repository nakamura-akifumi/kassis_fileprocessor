package info.tmpz84.app.kassis.fileprocessor.recv

import java.util.concurrent.CountDownLatch
import org.springframework.stereotype.Component
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import info.tmpz84.app.kassis.fileprocessor.ParseFile
import info.tmpz84.app.kassis.fileprocessor.domain.data.KassisFileMessage
import info.tmpz84.app.kassis.fileprocessor.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired

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

        val parseFile = ParseFile()
        parseFile.parse(kassisFileMessage)
        latch.countDown()
    }

}