package controllers

import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.model.objects.keyboard
import kotlinx.coroutines.runBlocking
import model.domain.Task
import model.domain.preview
import util.Messages
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ResponseSender(
    private val client: VkApiClient
) {
    fun taskCreationSetDate(userId: Int) {
        taskCreationDateState(userId, Messages.getMessage("TaskCreation.SetDate"))
    }

    fun taskCreationSetDateRetry(userId: Int) {
        taskCreationDateState(userId, Messages.getMessage("TaskCreation.SetDateRetry"))
    }

    private fun taskCreationDateState(userId: Int, messageToSend: String) = runBlocking {
        client.sendMessage {
            message = messageToSend
            peerId = userId
            keyboard = keyboard {
                row {
                    secondaryButton(Messages.getMessage("Buttons.Cancel"))
                }
            }
        }.execute()
    }

    fun taskCreationSetTime(userId: Int) {
        taskCreationTimeState(userId, Messages.getMessage("TaskCreation.SetTime"))
    }

    fun taskCreationSetTimeRetry(userId: Int) {
        taskCreationTimeState(userId, Messages.getMessage("TaskCreation.SetTimeRetry"))
    }

    private fun taskCreationTimeState(userId: Int, messageToSend: String) = runBlocking {
        client.sendMessage {
            message = messageToSend
            peerId = userId
            keyboard = keyboard {
                row {
                    secondaryButton(Messages.getMessage("Buttons.Cancel"))
                }
            }
        }.execute()
    }

    fun taskCreationSetRepeat(userId: Int) {
        taskCreationRepeatState(userId, Messages.getMessage("TaskCreation.SetRepeat"))
    }

    fun taskCreationSetRepeatRetry(userId: Int) {
        taskCreationRepeatState(userId, Messages.getMessage("TaskCreation.SetRepeatRetry"))
    }

    private fun taskCreationRepeatState(userId: Int, messageToSend: String) = runBlocking {
        client.sendMessage {
            message = messageToSend
            peerId = userId
            keyboard = keyboard {
                row {
                    secondaryButton(Messages.getMessage("TaskCreation.RepeatState.NoRepeatsButton"))
                }
                row {
                    secondaryButton(Messages.getMessage("TaskCreation.RepeatState.DailyButton"))
                }
                row {
                    secondaryButton(Messages.getMessage("TaskCreation.RepeatState.WeeklyButton"))
                }
                row {
                    secondaryButton(Messages.getMessage("TaskCreation.RepeatState.MonthlyButton"))
                }
                row {
                    secondaryButton(Messages.getMessage("TaskCreation.RepeatState.YearlyButton"))
                }
                row {
                    secondaryButton(Messages.getMessage("Buttons.Cancel"))
                }
            }
        }.execute()
    }

    fun taskCreationSetText(userId: Int) = runBlocking {
        client.sendMessage {
            message = Messages.getMessage("TaskCreation.setText")
            peerId = userId
            keyboard = keyboard {
                row {
                    secondaryButton(Messages.getMessage("Buttons.Cancel"))
                }
            }
        }.execute()
    }

    fun taskCreationConfirmation(userId: Int, taskInfo: Task?) {
        if (taskInfo == null) {
            taskCreationConfirmationState(userId, Messages.getMessage("TaskCreation.TaskPreviewNotFound"))
        } else {
            taskCreationConfirmationState(
                userId,
                Messages.getMessage("TaskCreation.Confirmation") + "\n" + taskInfo.preview()
            )
        }
    }

    fun taskCreationConfirmationRetry(userId: Int) {
        taskCreationConfirmationState(userId, Messages.getMessage("TaskCreation.ConfirmationRetry"))
    }

    private fun taskCreationConfirmationState(userId: Int, messageToSend: String) = runBlocking {
        client.sendMessage {
            message = messageToSend
            peerId = userId
            keyboard = keyboard {
                row {
                    secondaryButton(Messages.getMessage("Buttons.Confirm"))
                    secondaryButton(Messages.getMessage("Buttons.Cancel"))
                }
            }
        }.execute()
    }

    fun taskCreatedSuccessfully(userId: Int) {
        baseState(userId, Messages.getMessage("TaskCreatedSuccessfully"))
    }

    fun cancel(userId: Int) {
        baseState(userId, Messages.getMessage("CancelOperation"))
    }

    fun unknownMessage(userId: Int) {
        baseState(userId, Messages.getMessage("UnknownMessage"))
    }

    fun showDailyTasks(userId: Int, tasks: String) {
        if (tasks.isBlank()) {
            baseState(userId, Messages.getMessage("NoTasks"))
        } else {
            val today = DateTimeFormatter.ofPattern(Messages.getMessage("PreviewDateFormat")).format(LocalDate.now())
            baseState(userId, Messages.getMessage("DailyTasksList") + " (" + today + "):\n" + tasks)
        }

    }

    fun showWeeklyTasks(userId: Int, tasks: String) {
        if (tasks.isBlank()) {
            baseState(userId, Messages.getMessage("NoTasks"))
        } else {
            val monday = LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong()).plusDays(1)
            val sunday = monday.plusDays(7)
            val mondayString = DateTimeFormatter.ofPattern(Messages.getMessage("PreviewDateFormat")).format(monday)
            val sundayString = DateTimeFormatter.ofPattern(Messages.getMessage("PreviewDateFormat")).format(sunday)
            baseState(
                userId,
                Messages.getMessage("WeeklyTasksList") + " " + mondayString + " - " + sundayString + "\n" + tasks
            )
        }
    }

    fun showAllTasks(userId: Int, tasks: String) {
        if (tasks.isBlank()) {
            baseState(userId, Messages.getMessage("NoTasks"))
        } else {
            baseState(userId, Messages.getMessage("AllTasksList") + "\n" + tasks)
        }
    }

    private fun baseState(userId: Int, messageToSend: String) = runBlocking {
        client.sendMessage {
            message = messageToSend
            peerId = userId
            keyboard = keyboard {
                row {
                    secondaryButton(Messages.getMessage("Buttons.CreateTask"))
                }
                row {
                    secondaryButton(Messages.getMessage("Buttons.ShowDailyTasks"))
                }
                row {
                    secondaryButton(Messages.getMessage("Buttons.ShowWeeklyTasks"))
                }
                row {
                    secondaryButton(Messages.getMessage("Buttons.ShowAllTasks"))
                }
            }
        }.execute()
    }
}