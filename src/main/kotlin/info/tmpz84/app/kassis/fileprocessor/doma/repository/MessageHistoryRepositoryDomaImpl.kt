package info.tmpz84.app.kassis.fileprocessor.doma.repository

import info.tmpz84.app.kassis.fileprocessor.doma.dao.MessageHistoryDao
import info.tmpz84.app.kassis.fileprocessor.doma.dao.UserDao
import info.tmpz84.app.kassis.fileprocessor.doma.entity.MessageHistoryEntity
import info.tmpz84.app.kassis.fileprocessor.doma.entity.UserEntity
import info.tmpz84.app.kassis.fileprocessor.domain.model.MessageHistory
import info.tmpz84.app.kassis.fileprocessor.domain.model.User
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
class MessageHistoryRepositoryDomaImpl (
        private val dao: MessageHistoryDao) {

    fun create(messageHistory: MessageHistory): Int {
        val domaEntity = _mapToDomaEntity(messageHistory)
        dao.insert(domaEntity)
        return domaEntity.id
    }

    // ここでドメインのModel（Kotlin）をDomaのEntity（Java）をに詰め替える
    private fun _mapToDomaEntity(model: MessageHistory): MessageHistoryEntity {
        val timestamp = Timestamp(System.currentTimeMillis());

        return MessageHistoryEntity().also {
            it.id = model.id
            it.msgid = model.msgid
            it.row = model.row
            it.message_type = model.message_type
            it.status = model.status
            it.note = model.note
            it.created_at = timestamp
            it.updated_at = timestamp
        }
    }
}