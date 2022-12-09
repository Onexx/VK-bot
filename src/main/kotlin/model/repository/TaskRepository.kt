package model.repository

import model.domain.Repeats
import model.domain.Task
import java.time.LocalDate

interface TaskRepository {
    fun create(userId: Int)
    fun setDate(userId: Int, date: LocalDate)
    fun setText(userId: Int, text: String)
    fun setRepeat(userId: Int, repeat: Repeats)
    fun setCreationFinished(userId: Int, creationFinished: Boolean)
    fun removeUnfinishedTask(userId: Int)
    fun findTasksByAuthorId(authorId: Int): List<Task>
}