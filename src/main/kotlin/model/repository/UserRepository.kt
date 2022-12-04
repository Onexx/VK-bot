package model.repository

import model.domain.User

interface UserRepository {
    fun find(id: Long): User?
    fun findByLogin(login: String): User?
    fun findByEmail(email: String): User?
    fun findByLoginOrEmailAndPasswordSha(loginOrEmail: String, passwordSha: String): User?
    fun findAll(): List<User>
    fun save(user: User, passwordSha: String)
}