package info.tmpz84.app.kassis.fileprocessor.doma.repository

import info.tmpz84.app.kassis.fileprocessor.doma.dao.MessageAdapterDao
import info.tmpz84.app.kassis.fileprocessor.doma.entity.MessageAdapterEntity
import info.tmpz84.app.kassis.fileprocessor.domain.model.MessageAdapter
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
class MessageAdapterRepositoryDomaImpl (
        private val dao: MessageAdapterDao) {

    fun selectByMsgId(msgId: String):MessageAdapter {
        return dao.selectByMsgid(msgId).let { _mapToModel(it) }
    }

    fun update(model: MessageAdapter) {
        val domaEntity = _mapToDomaEntity(model)
        domaEntity.updated_at = Timestamp(System.currentTimeMillis())
        dao.update(domaEntity)
    }

    private fun _mapToModel(entity: MessageAdapterEntity): MessageAdapter {
        return MessageAdapter(
                id = entity.id,
                msgid = entity.msgid,
                title = entity.title,
                status = entity.status,
                state = entity.state,
                message_type = entity.message_type,
                created_by = entity.created_by,
                created_at = entity.created_at,
                updated_at = entity.updated_at
        )
    }

    private fun _mapToDomaEntity(model: MessageAdapter): MessageAdapterEntity {
        val timestamp = Timestamp(System.currentTimeMillis())

        return MessageAdapterEntity().also {
            it.id = model.id
            it.msgid = model.msgid
            it.title = model.title
            it.status = model.status
            it.state = model.state
            it.message_type = model.message_type
            it.created_by = model.created_by
            if (it.created_at == null) {
                it.created_at = timestamp
            }
            if (it.updated_at == null) {
                it.updated_at = timestamp
            }
        }
    }
}