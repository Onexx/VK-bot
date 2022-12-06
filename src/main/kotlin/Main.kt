import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.model.VkSettings
import com.petersamokhin.vksdk.core.model.objects.keyboard
import com.petersamokhin.vksdk.http.VkOkHttpClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import model.domain.DialogState.DIALOG
import model.domain.DialogState.TASK_CREATION_SET_TEXT
import model.service.StateService

@ExperimentalCoroutinesApi
fun main() {
    println("Server started")
    val groupId = System.getenv("GROUP_ID")?.toInt() ?: 0
    val accessToken = System.getenv("ACCESS_TOKEN") ?: ""

    val vkHttpClient = VkOkHttpClient()

    val client = VkApiClient(groupId, accessToken, VkApiClient.Type.Community, VkSettings(vkHttpClient))

    val stateService = StateService()

    client.onMessage { messageEvent ->
        println("mess: $messageEvent")
        when (stateService.getState(messageEvent.message.peerId)) {
            DIALOG -> runBlocking {
                when (messageEvent.message.text) {
                    "оставить состояние 1" -> client.sendMessage {
                        message = Messages.getMessage("dialogStateNotChanged")
                        peerId = messageEvent.message.peerId
                        keyboard = keyboard(oneTime = true) {
                            row {
                                secondaryButton("оставить состояние 1")
                                secondaryButton("изменить состояние 1")
                            }
                        }
                    }.execute()

                    "изменить состояние 1" -> {
                        stateService.saveState(messageEvent.message.peerId, TASK_CREATION_SET_TEXT)
                        client.sendMessage {
                            message = Messages.getMessage("ChangedToSecondState")
                            peerId = messageEvent.message.peerId
                            keyboard = keyboard(oneTime = true) {
                                row {
                                    secondaryButton("оставить состояние 2")
                                    secondaryButton("изменить состояние 2")
                                }
                            }
                        }.execute()
                    }
                    else -> {}
                }
            }
            TASK_CREATION_SET_TEXT -> runBlocking {
                when (messageEvent.message.text) {
                    "оставить состояние 2" -> client.sendMessage {
                        message = Messages.getMessage("dialogStateNotChanged2")
                        peerId = messageEvent.message.peerId
                        keyboard = keyboard(oneTime = true) {
                            row {
                                secondaryButton("оставить состояние 2")
                                secondaryButton("изменить состояние 2")
                            }
                        }
                    }.execute()

                    "изменить состояние 2" -> {
                        stateService.saveState(messageEvent.message.peerId, DIALOG)
                        client.sendMessage {
                            message = Messages.getMessage("ChangedToFirstState")
                            peerId = messageEvent.message.peerId
                            keyboard = keyboard(oneTime = true) {
                                row {
                                    secondaryButton("оставить состояние 1")
                                    secondaryButton("изменить состояние 1")
                                }
                            }
                        }.execute()
                    }
                    else -> {}
                }
            }
        }

    }

    runBlocking { client.startLongPolling() }
}