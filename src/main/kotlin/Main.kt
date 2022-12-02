import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.model.VkSettings
import com.petersamokhin.vksdk.http.VkOkHttpClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
fun main() {
    val groupId = -1
    val accessToken = "secret-token"

    val vkHttpClient = VkOkHttpClient()

    val client = VkApiClient(groupId, accessToken, VkApiClient.Type.Community, VkSettings(vkHttpClient))

    client.onMessage { messageEvent ->
        println("mess: $messageEvent")
        runBlocking {
            client.sendMessage {
                peerId = messageEvent.message.peerId
                message = "Hello, World!"

                // You can use stickers, replies, location, etc.
                // All of the message parameters are supported.
            }.execute()
        }
    }
    client.onEachEvent {
        println("$it")
    }

    runBlocking { client.startLongPolling() }
}