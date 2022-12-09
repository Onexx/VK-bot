package util

import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

object Messages {
    private val prop = Properties()

    init {
        try {
            val resourceStream: InputStream? = this.javaClass.classLoader.getResourceAsStream("messages.ru")
            if (resourceStream == null) {
                System.err.println("Couldn't load messages")
            }
            resourceStream?.use {
                prop.load(it.bufferedReader(Charsets.UTF_8))
            }
        } catch (e: FileNotFoundException) {
            System.err.println("Couldn't load config'")
        }
    }

    fun getMessage(messageKey: String): String {
        return prop.getProperty(messageKey) ?: messageKey
    }
}