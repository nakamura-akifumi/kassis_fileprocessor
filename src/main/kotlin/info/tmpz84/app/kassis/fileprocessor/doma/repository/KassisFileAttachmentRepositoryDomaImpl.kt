package info.tmpz84.app.kassis.fileprocessor.doma.repository

import info.tmpz84.app.kassis.fileprocessor.doma.dao.KassisFileAttachmentDao
import info.tmpz84.app.kassis.fileprocessor.doma.entity.KassisFileAttachmentEntity
import info.tmpz84.app.kassis.fileprocessor.domain.model.KassisFileAttachment
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
class KassisFileAttachmentRepositoryDomaImpl (
        private val dao: KassisFileAttachmentDao) {

    fun create(d: KassisFileAttachment): Int {
        val domaEntity = _mapToDomaEntity(d)
        dao.insert(domaEntity)
        return domaEntity.id
    }

    private fun _mapToDomaEntity(model: KassisFileAttachment): KassisFileAttachmentEntity {
        val timestamp = Timestamp(System.currentTimeMillis())

        return KassisFileAttachmentEntity().also {
            it.id = model.id
            it.msgid = model.msgid
            it.fileid = model.fileid
            it.filename = model.filename
            it.byte_size = model.byte_size
            it.checksum = model.checksum
            if (it.created_at == null) {
                it.created_at = timestamp
            }
            if (it.updated_at == null) {
                it.updated_at = timestamp
            }
        }
    }
}