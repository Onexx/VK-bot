package model.service


import model.domain.Repeats
import model.domain.Task
import model.repository.TaskRepository
import model.repository.impl.MariaDbTaskRepositoryImpl
import java.time.LocalDate

class TaskService(
    private val taskRepository: TaskRepository = MariaDbTaskRepositoryImpl()
) {

    fun createNewTask(userId: Int) {
        taskRepository.create(userId)
    }

    fun setDate(userId: Int, date: LocalDate) {
        taskRepository.setDate(userId, date)
    }

    fun setText(userId: Int, text: String) {
        taskRepository.setText(userId, text)
    }

    fun setRepeat(userId: Int, repeat: Repeats) {
        taskRepository.setRepeat(userId, repeat)
    }

    fun confirmTaskCreation(userId: Int) {
        taskRepository.setCreationFinished(userId, true)
    }

    fun getAllTasks(userId: Int): List<Task> {
        return taskRepository.findTasksByAuthorId(userId)
    }

    fun getUnfinishedTask(userId: Int): Task? {
        return taskRepository.findUnfinishedTaskByAuthorId(userId)
    }

    fun cancelTaskCreation(userId: Int) {
        taskRepository.removeUnfinishedTask(userId)
    }
}