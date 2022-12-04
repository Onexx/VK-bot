package model.service


import model.domain.User
import model.exception.ValidationException
import model.repository.UserRepository
import model.repository.impl.MariaDbUserRepositoryImpl

class UserService {
    private val userRepository: UserRepository = MariaDbUserRepositoryImpl()

    @Throws(ValidationException::class)
    fun validateRegistration(user: User, password: String, passwordConfirmation: String) {
        if (user.login == null || user.login!!.isBlank()) {
            throw ValidationException("Login is required")
        }
        if (!user.login!!.matches(Regex("[a-z]+"))) {
            throw ValidationException("Login can contain only lowercase Latin letters")
        }
        if (user.login!!.length > 8) {
            throw ValidationException("Login can't be longer than 8 letters")
        }
        if (userRepository.findByLogin(user.login!!) != null) {
            throw ValidationException("Login is already in use")
        }
    }

    fun register(user: User, password: String) {
        userRepository.save(user, getPasswordSha(password))
    }

    private fun getPasswordSha(password: String): String {
        return PASSWORD_SALT + password
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    @Throws(ValidationException::class)
    fun validateEnter(loginOrEmail: String, password: String) {
        val user: User = userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, getPasswordSha(password))
            ?: throw ValidationException("Invalid login/email or password")
    }

    fun findByLoginOrEmailAndPassword(loginOrEmail: String, password: String): User? {
        return userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, getPasswordSha(password))
    }

    companion object {
        private const val PASSWORD_SALT = "177d4b5f2e4f4edafa7404533973c04c513ac619"
    }
}