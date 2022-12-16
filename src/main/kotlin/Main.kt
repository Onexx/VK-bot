import com.petersamokhin.vksdk.core.api.botslongpoll.VkBotsLongPollApi
import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.http.HttpClientConfig
import com.petersamokhin.vksdk.core.model.VkSettings
import com.petersamokhin.vksdk.http.VkOkHttpClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

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

    val dependencies = Dependencies(client)

    client.onMessage { messageEvent ->
        dependencies.router.handleMessage(messageEvent)
    }

    runBlocking { client.startLongPolling(settings = VkBotsLongPollApi.Settings(wait = 25, maxFails = 1)) }
}