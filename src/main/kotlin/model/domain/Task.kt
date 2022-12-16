package model.domain

import model.domain.Repeats.NO_REPEATS
import java.io.Serializable
import java.time.LocalDate

class Task : Serializable {
    var id: Long = 0
    var authorId: Int = 0
    var date: LocalDate? = null
    var repeat: Repeats = NO_REPEATS
    var text: String = ""
    var creationFinished: Boolean = false
}