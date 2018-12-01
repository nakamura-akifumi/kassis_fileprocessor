package info.tmpz84.app.kassis.fileprocessor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import info.tmpz84.app.kassis.fileprocessor.doma.dao.MessageAdapterDao
import info.tmpz84.app.kassis.fileprocessor.doma.repository.MessageAdapterRepositoryDomaImpl
import info.tmpz84.app.kassis.fileprocessor.domain.DaoFactory
import info.tmpz84.app.kassis.fileprocessor.domain.model.KassisFileMessage
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

        val x:String = System.getProperty("kassis.bootDir","false")

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
                logger.info("filepath: ${kassisFileMessage.filepath}")
                logger.info("filename: ${kassisFileMessage.blob.filename}")
                logger.info("content_type: ${kassisFileMessage.blob.content_type}")

                val parseFile = ParseFileImpl()
                parseFile.ampqConnection = connection
                parseFile.parseManager(kassisFileMessage)


            }
        }
        channel.basicConsume(queueName, true, consumer)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ReceiverApplication::class.java, *args)
}
