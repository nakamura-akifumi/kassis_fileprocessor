package info.tmpz84.app.kassis.fileprocessor.doma.repository

import info.tmpz84.app.kassis.fileprocessor.doma.dao.UserDao
import info.tmpz84.app.kassis.fileprocessor.doma.entity.UserEntity
import info.tmpz84.app.kassis.fileprocessor.domain.model.User
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
class UserRepositoryDomaImpl (
        private val userDao: UserDao) {

    fun findAll(): List<User> {
        return userDao.selectAll().map { _mapToModel(it) }
    }

    fun create(user: User): Int {
        val domaEntity = _mapToDomaEntity(user)
        userDao.insert(domaEntity)
        return domaEntity.id
    }

    // ここでDomaのEntity（Java）をドメインのModel（Kotlin）に詰め替える
    private fun _mapToModel(domaEntity: UserEntity): User {
        return User(
                id = domaEntity.id,
                username = domaEntity.username,
                email = domaEntity.email,
                personid = domaEntity.personid,
                cardid = domaEntity.cardid,
                full_name = domaEntity.full_name,
                full_name_transcription = domaEntity.full_name_transcription,
                note = domaEntity.note
        )
    }

    // ここでドメインのModel（Kotlin）をDomaのEntity（Java）をに詰め替える
    private fun _mapToDomaEntity(user: User): UserEntity {
        val timestamp = Timestamp(System.currentTimeMillis());
        if (user.personid == null) {
            user.personid = UUID.randomUUID().toString()
        }

        return UserEntity().also {
            it.id = user.id
            it.username = user.username
            it.email = user.email
            it.personid = user.personid
            it.cardid = user.cardid
            it.full_name = user.full_name
            it.full_name_transcription = user.full_name_transcription
            it.note = user.note
            it.created_at = timestamp
            it.updated_at = timestamp

        }
    }
}