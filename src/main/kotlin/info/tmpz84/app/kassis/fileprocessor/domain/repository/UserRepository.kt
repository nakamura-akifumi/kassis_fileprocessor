package info.tmpz84.app.kassis.fileprocessor.domain.repository

import info.tmpz84.app.kassis.fileprocessor.domain.model.User

interface UserRepository {
    fun findAll(): List<User>
    fun create(user: User): Int
}