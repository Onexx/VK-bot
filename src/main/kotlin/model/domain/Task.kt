package model.domain

import model.domain.Repeats.NO_REPEATS
import util.Messages
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Task : Serializable {
    var id: Long = 0
    var authorId: Int = 0
    var date: LocalDate? = null
    var repeat: Repeats = NO_REPEATS
    var text: String = ""
    var creationFinished: Boolean = false
}

fun Task.preview(): String {
    return "${
        DateTimeFormatter.ofPattern(Messages.getMessage("PreviewDateFormat")).format(date)
    } [${Messages.getMessage("TaskInfo.Repeat.${repeat.name}")}] - \"$text\""
}