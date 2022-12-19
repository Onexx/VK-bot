package controllers

import com.petersamokhin.vksdk.core.model.event.MessageNew

class CommonController(
    private val responseSender: ResponseSender
) {
    fun begin(messageEvent: MessageNew) {
        responseSender.beginMessage(messageEvent.message.peerId)
    }

    fun unknown(messageEvent: MessageNew) {
        responseSender.unknownMessage(messageEvent.message.peerId)
    }
}