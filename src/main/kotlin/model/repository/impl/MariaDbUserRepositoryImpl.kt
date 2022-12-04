package model.repository.impl

import model.database.DatabaseUtils
import model.domain.User
import model.exception.RepositoryException
import model.repository.UserRepository
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Statement
import java.util.*
import javax.sql.DataSource

class MariaDbUserRepositoryImpl : UserRepository {

    private val DATA_SOURCE: DataSource = DatabaseUtils.getDataSource()

    override fun find(id: Long): User? {
        try {
            DATA_SOURCE.connection.use { connection ->
                connection.prepareStatement("SELECT * FROM User WHERE id=?").use { statement ->
                    statement.setLong(1, id)
                    statement.executeQuery().use { resultSet -> return toUser(statement.metaData, resultSet) }
                }
            }
        } catch (e: SQLException) {
            throw RepositoryException("Can't find User.", e)
        }
    }

    override fun findByLogin(login: String): User? {
        try {
            DATA_SOURCE.connection.use { connection ->
                connection.prepareStatement("SELECT * FROM User WHERE login=?").use { statement ->
                    statement.setString(1, login)
                    statement.executeQuery().use { resultSet -> return toUser(statement.metaData, resultSet) }
                }
            }
        } catch (e: SQLException) {
            throw RepositoryException("Can't find User.", e)
        }
    }

    override fun findByEmail(email: String): User? {
        try {
            DATA_SOURCE.connection.use { connection ->
                connection.prepareStatement("SELECT * FROM User WHERE email=?").use { statement ->
                    statement.setString(1, email)
                    statement.executeQuery().use { resultSet -> return toUser(statement.metaData, resultSet) }
                }
            }
        } catch (e: SQLException) {
            throw RepositoryException("Can't find User.", e)
        }
    }

    override fun findByLoginOrEmailAndPasswordSha(loginOrEmail: String, passwordSha: String): User? {
        try {
            DATA_SOURCE.connection.use { connection ->
                connection.prepareStatement("SELECT * FROM User WHERE (login=? OR email=?) AND passwordSha=?")
                    .use { statement ->
                        statement.setString(1, loginOrEmail)
                        statement.setString(2, loginOrEmail)
                        statement.setString(3, passwordSha)
                        statement.executeQuery().use { resultSet -> return toUser(statement.metaData, resultSet) }
                    }
            }
        } catch (e: SQLException) {
            throw RepositoryException("Can't find User.", e)
        }
    }

    override fun findAll(): List<User> {
        val users: MutableList<User> = ArrayList<User>()
        try {
            DATA_SOURCE.connection.use { connection ->
                connection.prepareStatement("SELECT * FROM User ORDER BY id DESC").use { statement ->
                    statement.executeQuery().use { resultSet ->
                        var user: User? = toUser(statement.metaData, resultSet)
                        while (user != null) {
                            users.add(user)
                            user = toUser(statement.metaData, resultSet)
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            throw RepositoryException("Can't find User.", e)
        }
        return users
    }

    @Throws(SQLException::class)
    private fun toUser(metaData: ResultSetMetaData, resultSet: ResultSet): User? {
        if (!resultSet.next()) {
            return null
        }
        val user = User()
        for (i in 1..metaData.columnCount) {
            when (metaData.getColumnName(i)) {
                "id" -> user.id = resultSet.getLong(i)
                "login" -> user.login = resultSet.getString(i)
                "email" -> user.email = resultSet.getString(i)
                "creationTime" -> user.creationTime = resultSet.getTimestamp(i)
                else -> {}
            }
        }
        return user
    }

    override fun save(user: User, passwordSha: String) {
        try {
            DATA_SOURCE.connection.use { connection ->
                connection.prepareStatement(
                    "INSERT INTO `User` (`login`, `email`, `passwordSha`, `creationTime`) VALUES (?, ?, ?, NOW())",
                    Statement.RETURN_GENERATED_KEYS
                ).use { statement ->
                    statement.setString(1, user.login)
                    statement.setString(2, user.email)
                    statement.setString(3, passwordSha)
                    if (statement.executeUpdate() != 1) {
                        throw RepositoryException("Can't save User.")
                    } else {
                        val generatedKeys = statement.generatedKeys
                        if (generatedKeys.next()) {
                            user.id = generatedKeys.getLong(1)
                            user.creationTime = find(user.id)?.creationTime
                        } else {
                            throw RepositoryException("Can't save User [no autogenerated fields].")
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            throw RepositoryException("Can't save User.", e)
        }
    }
}