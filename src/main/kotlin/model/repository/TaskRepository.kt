package model.repository

import model.domain.Repeats
import model.domain.Task
import java.time.LocalDate
import java.time.LocalTime

interface TaskRepository {
    fun create(userId: Int)
    fun setDate(userId: Int, date: LocalDate)
    fun setTime(userId: Int, time: LocalTime)
    fun setRepeat(userId: Int, repeat: Repeats)
    fun setText(userId: Int, text: String)
    fun setCreationFinished(userId: Int, creationFinished: Boolean)
    fun removeUnfinishedTask(userId: Int)
    fun deleteTaskById(userId: Int, taskId: Long)
    fun findTasksByAuthorId(authorId: Int): List<Task>
    fun findUnfinishedTaskByAuthorId(authorId: Int): Task?
}