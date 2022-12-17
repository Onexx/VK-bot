package controllers

import com.petersamokhin.vksdk.core.model.event.MessageNew
import model.domain.DialogState.*
import model.domain.Repeats
import model.domain.Repeats.*
import model.domain.preview
import model.service.StateService
import model.service.TaskService
import util.InputMessages
import util.Parsers
import java.util.*
import java.util.stream.Collectors

class TaskController(
    private val stateService: StateService,
    private val taskService: TaskService,
    private val responseSender: ResponseSender
) {

    fun startTaskCreation(messageEvent: MessageNew) {
        val userId = messageEvent.message.peerId
        stateService.saveState(userId, TASK_CREATION_SET_DATE)
        taskService.createNewTask(userId)
        responseSender.taskCreationSetDate(userId)
    }

    fun showAllTasks(messageEvent: MessageNew) {
        val userId = messageEvent.message.peerId
        val tasks = taskService.getAllTasks(userId).stream()
            .map { task -> task.preview() }
            .collect(Collectors.joining("\n"))

        responseSender.showAllTasks(userId, tasks)
    }

    fun setDate(messageEvent: MessageNew) {
        if (messageEvent.message.text.lowercase(Locale.getDefault()) in InputMessages.getMessages("Cancel")) {
            println("cancel")
            cancel(messageEvent)
            return
        }

        val userId = messageEvent.message.peerId
        val date = Parsers.parseDate(messageEvent.message.text.lowercase(Locale.getDefault()))
        if (date != null) {
            stateService.saveState(userId, TASK_CREATION_SET_REPEAT)
            taskService.setDate(userId, date)
            responseSender.taskCreationSetRepeat(userId)
        } else {
            responseSender.taskCreationSetDateRetry(userId)
        }
    }

    fun setRepeat(messageEvent: MessageNew) {
        if (messageEvent.message.text.lowercase(Locale.getDefault()) in InputMessages.getMessages("Cancel")) {
            cancel(messageEvent)
            return
        }

        var repeat: Repeats? = null
        val userId = messageEvent.message.peerId
        when (messageEvent.message.text.lowercase(Locale.getDefault())) {
            in InputMessages.getMessages("NoRepeats") -> repeat = NO_REPEATS
            in InputMessages.getMessages("RepeatDaily") -> repeat = DAILY
            in InputMessages.getMessages("RepeatWeekly") -> repeat = WEEKLY
            in InputMessages.getMessages("RepeatMonthly") -> repeat = MONTHLY
            in InputMessages.getMessages("RepeatYearly") -> repeat = YEARLY
        }

        if (repeat != null) {
            stateService.saveState(userId, TASK_CREATION_SET_TEXT)
            taskService.setRepeat(userId, repeat)
            responseSender.taskCreationSetText(userId)
        } else {
            responseSender.taskCreationSetRepeatRetry(userId)
        }
    }

    fun setText(messageEvent: MessageNew) {
        if (messageEvent.message.text.lowercase(Locale.getDefault()) in InputMessages.getMessages("Cancel")) {
            cancel(messageEvent)
            return
        }

        val userId = messageEvent.message.peerId

        stateService.saveState(userId, TASK_CREATION_CONFIRMATION)
        taskService.setText(userId, messageEvent.message.text)
        val taskInfo = taskService.getUnfinishedTask(userId)
        responseSender.taskCreationConfirmation(userId, taskInfo)
    }

    fun confirmation(messageEvent: MessageNew) {
        val userId = messageEvent.message.peerId
        when (messageEvent.message.text.lowercase(Locale.getDefault())) {
            in InputMessages.getMessages("Confirm") -> {
                stateService.saveState(userId, DIALOG)
                taskService.confirmTaskCreation(userId)
                responseSender.taskCreatedSuccessfully(userId)
            }
            in InputMessages.getMessages("Cancel") -> {
                cancel(messageEvent)
            }
            else -> {
                responseSender.taskCreationConfirmationRetry(userId)
            }
        }
    }

    private fun cancel(messageEvent: MessageNew) {
        val userId = messageEvent.message.peerId
        stateService.saveState(userId, DIALOG)
        taskService.cancelTaskCreation(userId)
        responseSender.cancel(userId)
    }
}