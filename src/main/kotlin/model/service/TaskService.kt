package model.service

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import model.domain.Repeats
import model.domain.Task
import model.repository.TaskRepository
import model.repository.impl.MariaDbTaskRepositoryImpl
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class TaskService(
    private val taskRepository: TaskRepository = MariaDbTaskRepositoryImpl()
) {
    private val loader: CacheLoader<Int, List<Task>> =
        CacheLoader.from { userId -> taskRepository.findTasksByAuthorId(userId) }

    private val cache: LoadingCache<Int, List<Task>> = CacheBuilder.newBuilder()
        .weigher { _: Int, value: List<Task> -> value.size }
        .maximumWeight(100_000)
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .build(loader)

    fun createNewTask(userId: Int) {
        taskRepository.create(userId)
    }

    fun setDate(userId: Int, date: LocalDate) {
        taskRepository.setDate(userId, date)
    }

    fun setTime(userId: Int, time: LocalTime) {
        taskRepository.setTime(userId, time)
    }

    fun setRepeat(userId: Int, repeat: Repeats) {
        taskRepository.setRepeat(userId, repeat)
    }

    fun setText(userId: Int, text: String) {
        taskRepository.setText(userId, text)
    }

    fun confirmTaskCreation(userId: Int) {
        cache.invalidate(userId)
        taskRepository.setCreationFinished(userId, true)
    }

    fun getAllTasks(userId: Int): List<Task> {
        return cache.get(userId)
    }

    fun getUnfinishedTask(userId: Int): Task? {
        return taskRepository.findUnfinishedTaskByAuthorId(userId)
    }

    fun cancelTaskCreation(userId: Int) {
        taskRepository.removeUnfinishedTask(userId)
    }
}