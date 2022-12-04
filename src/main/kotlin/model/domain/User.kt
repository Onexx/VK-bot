package model.domain

import java.io.Serializable
import java.util.*

class User : Serializable {
    var id: Long = 0
    var login: String? = null
    var email: String? = null
    var creationTime: Date? = null
}