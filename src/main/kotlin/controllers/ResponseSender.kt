package controllers

import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.model.objects.keyboard
import kotlinx.coroutines.runBlocking
import util.Messages

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

    fun taskCreationConfirmation(userId: Int) {
        taskCreationConfirmationState(userId, Messages.getMessage("TaskCreation.Confirmation"))
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

    private fun baseState(userId: Int, messageToSend: String) = runBlocking {
        client.sendMessage {
            message = messageToSend
            peerId = userId
            keyboard = keyboard {
                row {
                    secondaryButton(Messages.getMessage("Buttons.CreateTask"))
                }
            }
        }.execute()
    }
}