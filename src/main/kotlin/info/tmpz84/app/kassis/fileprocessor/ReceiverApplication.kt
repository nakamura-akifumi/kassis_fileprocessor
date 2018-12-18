package info.tmpz84.app.kassis.fileprocessor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import info.tmpz84.app.kassis.fileprocessor.domain.data.KassisFileMessage
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

@SpringBootConfiguration
@EnableAutoConfiguration
class ReceiverApplication : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        val queueName = "kassis_soda_development"

        logger.info("Start ReceiverApplication. queueName: $queueName")

        //val x:String = System.getProperty("kassis.bootDir","false")

        val factory = ConnectionFactory()
        factory.host = "localhost"
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(queueName, false, false, false, null)
        logger.info(" [*] Waiting for messages.")

        val consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {

                val json = String(body, charset("UTF-8"))
                val mapper = jacksonObjectMapper()
                val kassisFileMessage = mapper.readValue<KassisFileMessage>(json)

                logger.info("Received <$json>")
                logger.info("msgid: ${kassisFileMessage.msgid}")
                logger.info("message_type: ${kassisFileMessage.message_type}")
                logger.info("filepath: ${kassisFileMessage.filepath}")

                if (kassisFileMessage.blob != null) {
                    logger.info("filename: ${kassisFileMessage.blob.filename}")
                    logger.info("content_type: ${kassisFileMessage.blob.content_type}")
                }

                when (kassisFileMessage.message_type) {
                    "user.file.import" -> {
                        val parseFile = ParseFileImpl()
                        parseFile.amqpConnection = connection
                        parseFile.parseManager(kassisFileMessage)
                    }
                    "user.file.export" -> {
                        val parseFile = ParseFileImpl()
                        parseFile.amqpConnection = connection
                        parseFile.buildManager(kassisFileMessage)
                    }
                }


            }
        }
        channel.basicConsume(queueName, true, consumer)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ReceiverApplication::class.java, *args)
}
