import com.petersamokhin.vksdk.core.model.event.MessageNew
import controllers.CommonController
import controllers.TaskController
import model.domain.DialogState
import model.domain.DialogState.*
import model.service.StateService
import util.InputMessages
import java.util.*

class Router(
    private val stateService: StateService,
    private val taskController: TaskController,
    private val commonController: CommonController
) {
    fun handleMessage(messageEvent: MessageNew) {
        messageEvent.clientInfo.keyboard
        val state = stateService.getState(messageEvent.message.peerId)

        if (state == DIALOG) {
            messageDeterminer(messageEvent)
        } else {
            stateDeterminer(state, messageEvent)
        }
    }

    private fun messageDeterminer(messageEvent: MessageNew) {
        when (messageEvent.message.text.lowercase(Locale.getDefault())) {
            in InputMessages.getMessages("Begin") -> commonController.begin(messageEvent)
            in InputMessages.getMessages("CreateTask") -> taskController.startTaskCreation(messageEvent)
            in InputMessages.getMessages("ShowDailyTasks") -> taskController.showDailyTasks(messageEvent)
            in InputMessages.getMessages("ShowWeeklyTasks") -> taskController.showWeeklyTasks(messageEvent)
            in InputMessages.getMessages("ShowAllTasks") -> taskController.showAllTasks(messageEvent)
            in InputMessages.getMessages("DeleteTask") -> taskController.startTaskDeletion(messageEvent)
            else -> commonController.unknown(messageEvent)
        }
    }

    private fun stateDeterminer(state: DialogState, messageEvent: MessageNew) {
        when (state) {
            TASK_CREATION_SET_DATE -> taskController.setDate(messageEvent)
            TASK_CREATION_SET_TIME -> taskController.setTime(messageEvent)
            TASK_CREATION_SET_REPEAT -> taskController.setRepeat(messageEvent)
            TASK_CREATION_SET_TEXT -> taskController.setText(messageEvent)
            TASK_CREATION_CONFIRMATION -> taskController.confirmation(messageEvent)
            TASK_DELETION -> taskController.deleteTask(messageEvent)
            DIALOG -> throw IllegalStateException("DIALOG state is not allowed in stateDeterminer")
        }
    }
}