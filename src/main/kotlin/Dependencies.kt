import com.petersamokhin.vksdk.core.client.VkApiClient
import controllers.CommonController
import controllers.ResponseSender
import controllers.TaskController
import model.service.StateService
import model.service.TaskService

class Dependencies(
    client: VkApiClient
) {
    private val responseSender = ResponseSender(client)
    private val stateService = StateService()
    private val taskService = TaskService()
    private val taskController = TaskController(stateService, taskService, responseSender)
    private val commonController = CommonController(responseSender)

    val router = Router(stateService, taskController, commonController)
}