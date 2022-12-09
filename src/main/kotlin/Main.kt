import com.petersamokhin.vksdk.core.api.botslongpoll.VkBotsLongPollApi
import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.http.HttpClientConfig
import com.petersamokhin.vksdk.core.model.VkSettings
import com.petersamokhin.vksdk.http.VkOkHttpClient
import controllers.CommonController
import controllers.ResponseSender
import controllers.TaskController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import model.service.StateService
import model.service.TaskService

@ExperimentalCoroutinesApi
fun main() {
    println("Server started")

    val vkHttpClient = VkOkHttpClient(
        HttpClientConfig(
            readTimeout = 30_000,
            connectTimeout = 30_000
        )
    )

    val groupId = System.getenv("GROUP_ID")?.toInt() ?: 0
    val accessToken = System.getenv("ACCESS_TOKEN") ?: ""

    val client = VkApiClient(groupId, accessToken, VkApiClient.Type.Community, VkSettings(vkHttpClient))

    val responseSender = ResponseSender(client)
    val stateService = StateService()
    val taskService = TaskService()
    val taskController = TaskController(stateService, taskService, responseSender)
    val commonController = CommonController(responseSender)
    val router = Router(stateService, taskController, commonController)

    client.onMessage { messageEvent ->
        router.handleMessage(messageEvent)
    }

    runBlocking { client.startLongPolling(settings = VkBotsLongPollApi.Settings(wait = 25, maxFails = 1)) }
}