import com.petersamokhin.vksdk.core.api.botslongpoll.VkBotsLongPollApi
import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.error.VkResponseException
import com.petersamokhin.vksdk.core.model.VkSettings
import com.petersamokhin.vksdk.http.VkOkHttpClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
fun main() {
    println("Server started")

    val groupId = System.getenv("GROUP_ID")?.toInt() ?: 0
    val accessToken = System.getenv("ACCESS_TOKEN") ?: ""

    while (true) {
        val vkHttpClient = VkOkHttpClient(
            OkHttpClient.Builder()
                .readTimeout(30_000, TimeUnit.MILLISECONDS)
                .connectTimeout(30_000, TimeUnit.MILLISECONDS)
                .writeTimeout(30_000, TimeUnit.MILLISECONDS)
                .pingInterval(1, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder().build()

                    chain.proceed(newRequest)
                }
                .build()
        )
        val client = VkApiClient(groupId, accessToken, VkApiClient.Type.Community, VkSettings(vkHttpClient))

        val dependencies = Dependencies(client)

        client.onMessage { messageEvent ->
            dependencies.router.handleMessage(messageEvent)
        }
        try {
            runBlocking {
                client.startLongPolling(settings = VkBotsLongPollApi.Settings(wait = 25, maxFails = 0))
            }
        } catch (e: VkResponseException) {
            System.err.println("longPolling failed with exception: $e")
            System.err.println("Restarting server")
        }
    }
}