package info.tmpz84.app.kassis.fileprocessor

import java.io.File
import java.io.InputStream
import com.monitorjbl.xlsx.StreamingReader;
import info.tmpz84.app.kassis.fileprocessor.doma.dao.MessageAdapterDao
import info.tmpz84.app.kassis.fileprocessor.doma.dao.UserDao
import info.tmpz84.app.kassis.fileprocessor.doma.repository.MessageAdapterRepositoryDomaImpl
import info.tmpz84.app.kassis.fileprocessor.doma.repository.UserRepositoryDomaImpl
import info.tmpz84.app.kassis.fileprocessor.domain.DaoFactory
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.io.FileInputStream
import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.Connection
import info.tmpz84.app.kassis.fileprocessor.doma.dao.KassisFileAttachmentDao
import info.tmpz84.app.kassis.fileprocessor.doma.dao.MessageHistoryDao
import info.tmpz84.app.kassis.fileprocessor.doma.repository.KassisFileAttachmentRepositoryDomaImpl
import info.tmpz84.app.kassis.fileprocessor.doma.repository.MessageHistoryRepositoryDomaImpl
import info.tmpz84.app.kassis.fileprocessor.domain.data.KassisFileMessage
import info.tmpz84.app.kassis.fileprocessor.domain.data.KassisFileProcessMessage
import info.tmpz84.app.kassis.fileprocessor.domain.model.*
import org.apache.commons.codec.digest.DigestUtils
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.util.*


@Configuration
@Service
class ParseFileImpl: ParseFile {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val exchangeName = "messageExchange"

    lateinit var amqpConnection: Connection

    override fun buildManager(kassisFileMessage: KassisFileMessage): Int {
        // TODO: 設定ファイルから読み込む
        val sendQueueName = "kassis_soda_development"
        //val factory = ConnectionFactory()
        //factory.host = "localhost"
        //val connection = factory.newConnection()
        val channel = amqpConnection.createChannel()

        channel.exchangeDeclare("messageExchange", "topic")

        val dao = DaoFactory.create(MessageAdapterDao::class)
        val repo = MessageAdapterRepositoryDomaImpl(dao)

        val historyDao = DaoFactory.create(MessageHistoryDao::class)
        val historyRepository = MessageHistoryRepositoryDomaImpl(historyDao)

        val daoFile = DaoFactory.create(KassisFileAttachmentDao::class)
        val repoFile = KassisFileAttachmentRepositoryDomaImpl(daoFile)
        val tm = ConfigAdapter.transactionManager
        var messageAdapter: MessageAdapter
        tm.required {
            messageAdapter = repo.selectByMsgId(kassisFileMessage.msgid)
            messageAdapter.state = "processing"
            repo.update(messageAdapter)
        }

        val mapper = ObjectMapper()
        var msgObj = KassisFileProcessMessage(
                kassisFileMessage.msgid,
                "processing",
                "エクスポート処理を開始しました。",
                -1F,
                0,
                0
        )

        var json = mapper.writeValueAsString(msgObj)

        channel.basicPublish(exchangeName,
                "kassis.file.replay_messages.${kassisFileMessage.msgid}",
                null,
                json.toByteArray())

        val (lines, kassisFileAttachment) = buildExcelFile(kassisFileMessage)

        logger.info("complete build: lines=${lines} path=${kassisFileAttachment.exportfullpath}")

        tm.required {
            messageAdapter = repo.selectByMsgId(kassisFileMessage.msgid)

            messageAdapter.state = "processed"
            messageAdapter.status = "success"
            repo.update(messageAdapter)

            repoFile.create(kassisFileAttachment)
        }
        msgObj = KassisFileProcessMessage(
                kassisFileMessage.msgid,
                "completed",
                "エクスポート処理を完了しました。",
                -1F,
                0,
                0
        )

        json = mapper.writeValueAsString(msgObj)

        channel.basicPublish(exchangeName,
                "kassis.file.replay_messages",
                null,
                json.toByteArray())

        return lines
    }

    override fun buildExcelFile(kassisFileMessage: KassisFileMessage): Pair<Int, KassisFileAttachment> {

        val tm = ConfigAdapter.transactionManager

        val userDao = DaoFactory.create(UserDao::class)
        val userRepository = UserRepositoryDomaImpl(userDao)

        var processLine = 0
        var filePath = ""
        var fileName = ""
        val fileID = UUID.randomUUID().toString().replace("-", "")

        val channel = amqpConnection.createChannel()
        channel.exchangeDeclare("messageExchange", "topic")

        val msgId = kassisFileMessage.msgid

        val COLUMNs = arrayOf<String>(
        "PersonID","ログインID","利用者番号","フルネーム","フルネームカナ","有効期限",
        "登録日","注記","無効フラグ","無効日時","パスワード",	"メールアドレス",
        "連絡先１","連絡先１補助","連絡先１区分","連絡先１注記",
        "連絡先２","連絡先２補助","連絡先２区分","連絡先２注記"
        )

        tm.required {
            val users = userRepository.findAll()

            val workbook = XSSFWorkbook()
            val createHelper = workbook.getCreationHelper()

            val sheet = workbook.createSheet("Users")

            val headerFont = workbook.createFont()
            headerFont.setBold(true)

            val headerCellStyle = workbook.createCellStyle()
            headerCellStyle.setFont(headerFont)

            // Row for Header
            val headerRow = sheet.createRow(0)

            // Header
            for (col in COLUMNs.indices) {
                val cell = headerRow.createCell(col)
                cell.setCellValue(COLUMNs[col])
                cell.setCellStyle(headerCellStyle)
            }

            // CellStyle for Age
            val ageCellStyle = workbook.createCellStyle()
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"))

            var rowIdx = 1
            for (user in users) {
                val row = sheet.createRow(rowIdx++)
                row.createCell(0).setCellValue(user.personid)
                row.createCell(1).setCellValue(user.username)
                row.createCell(2).setCellValue(user.cardid)
                row.createCell(3).setCellValue(user.full_name)
                row.createCell(4).setCellValue(user.full_name_transcription)
                row.createCell(5).setCellValue(user.email)

                processLine++
            }

            fileName = fileID+".xlsx"
            filePath = File(kassisFileMessage.filepath, fileName).getPath()

            logger.info("build excel file: ${filePath}")

            val fileOut = FileOutputStream(filePath)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()
        }

        val fileLength: Long = File(filePath).length()
        val checksumString = DigestUtils.md5Hex(FileInputStream(File(filePath)))

        val kassisAttachment = KassisFileAttachment(
                msgid = kassisFileMessage.msgid,
                fileid = fileID,
                filename = fileName,
                exportfullpath = filePath,
                byte_size = fileLength,
                checksum = checksumString)

        return Pair(processLine, kassisAttachment)
    }

    override fun parseManager(kassisFileMessage: KassisFileMessage): Int {

        // TODO: 設定ファイルから読み込む
        val sendQueueName = "kassis_soda_development"
        //val factory = ConnectionFactory()
        //factory.host = "localhost"
        //val connection = factory.newConnection()
        val channel = amqpConnection.createChannel()

        channel.exchangeDeclare("messageExchange", "topic")

        val dao = DaoFactory.create(MessageAdapterDao::class)
        val repo = MessageAdapterRepositoryDomaImpl(dao)
        val tm = ConfigAdapter.transactionManager
        var messageAdapter: MessageAdapter
        tm.required {
            messageAdapter = repo.selectByMsgId(kassisFileMessage.msgid)
            /*
            if (messageAdapter == null) {
                logger.warn("can not find message_adapter. msgId:${kassisFileMessage.msgid}")
                //return 0
            }
            */
            // TODO: status = set だけ対応するか
            // TODO: 要リファクタリング
            // state: set -> (accepted) -> processing -> processed
            // status: normal -> error or sucess
            messageAdapter.state = "processing"
            repo.update(messageAdapter)
        }

        val mapper = ObjectMapper()
        var msgObj = KassisFileProcessMessage(
                kassisFileMessage.msgid,
                "processing",
                "インポート処理を開始しました。",
                -1F,
                0,
                0
        )

        var json = mapper.writeValueAsString(msgObj)

        channel.basicPublish(exchangeName,
                "kassis.file.replay_messages.${kassisFileMessage.msgid}",
                null,
                json.toByteArray())

        val lines = parseExcelFile(kassisFileMessage)

        tm.required {
            messageAdapter = repo.selectByMsgId(kassisFileMessage.msgid)

            messageAdapter.state = "processed"
            messageAdapter.status = "success"
            repo.update(messageAdapter)
        }
        msgObj = KassisFileProcessMessage(
                kassisFileMessage.msgid,
                "completed",
                "インポート処理を完了しました。",
                -1F,
                0,
                0
        )

        json = mapper.writeValueAsString(msgObj)

        channel.basicPublish(exchangeName,
                "kassis.file.replay_messages",
                null,
                json.toByteArray())

        return lines
    }

    override fun parseExcelFile(kassisFileMessage: KassisFileMessage):Int {

        val ldapService = LdapService()
        ldapService.connect()

        val userDao = DaoFactory.create(UserDao::class)
        val userRepository = UserRepositoryDomaImpl(userDao)

        val historyDao = DaoFactory.create(MessageHistoryDao::class)
        val historyRepository = MessageHistoryRepositoryDomaImpl(historyDao)

        val filefullpath:String = kassisFileMessage.filepath

        val inputStream:InputStream = FileInputStream(File(filefullpath))
        val workbook:Workbook = StreamingReader.builder()
                .rowCacheSize(100)   // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)       // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(inputStream)                // InputStream or File for XLSX file (required)

        val tm = ConfigAdapter.transactionManager

        val channel = amqpConnection.createChannel()
        channel.exchangeDeclare("messageExchange", "topic")

        val msgId = kassisFileMessage.msgid

        var maxcol = 0
        var line:Int?
        var processLine = 0
        for (sheet:Sheet in workbook) {
            line = 0

            for (row in sheet) {

                line++
                if (line == 1) {
                    //タイトル行
                    for ((index, c) in row.withIndex()) {
                        System.out.println("$index ${c.stringCellValue}")
                        maxcol = index
                    }

                } else {

                    //情報行
                    var u = User()
                    for (index in 0..maxcol) {
                        val cell = row.getCell(index)
                        if (cell == null) {
                            continue
                        }
                        if (cell.stringCellValue.trim() == "" || cell.stringCellValue == null) {
                            //println("empty ${index}")
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
                                //println("password:${index}:${cell.stringCellValue}")
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

                    var errorFlag = false
                    var note = ""
                    var note2:String? = ""
                    tm.required {
                        try {
                            userRepository.create(u)

                            if (u?.username != null) {
                                println("password=${u.password}")
                                ldapService.create(u)
                            }
                        } catch (e: org.seasar.doma.jdbc.UniqueConstraintException) {
                            errorFlag = true
                            tm.setRollbackOnly()
                            logger.info("${line} 行目で一意制約違反エラーが発生しました。")
                            //logger.info(e.localizedMessage)
                            note = "${line} 行目で一意制約違反エラー(${e.kind})が発生しました。"
                            note2 = e.cause.toString()
                        }
                    }
                    if (errorFlag) {
                        val mhistory = MessageHistory(msgid = msgId,
                                row = Integer.toString(line),
                                note = note,
                                note2 = note2)

                        tm.required {
                            historyRepository.create(mhistory)
                        }
                    }
                    processLine++

                    var msgObj = KassisFileProcessMessage(
                            kassisFileMessage.msgid,
                            "processing",
                            "${processLine} 処理中です。",
                            -1F,
                            processLine,
                            0
                    )

                    val mapper = ObjectMapper()
                    var json = mapper.writeValueAsString(msgObj)

                    channel.basicPublish("messageExchange",
                            "kassis.file.replay_messages.${kassisFileMessage.msgid}",
                            null,
                            json.toByteArray())
                }

            }
        }

        // TODO: finnaly とかで実行する
        ldapService.disconnect()

        return processLine

    }
}
