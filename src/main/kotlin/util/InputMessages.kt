package util

import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

object InputMessages {
    private val prop = Properties()

    init {
        try {
            val resourceStream: InputStream? = this.javaClass.classLoader.getResourceAsStream("inputs.ru")
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

    fun getMessages(messageKey: String): List<String> {
        val result = ArrayList<String>()
        if (prop.getProperty(messageKey) != null) {
            result.add(prop.getProperty(messageKey))
        }
        var idx = 1
        while (prop.getProperty("$messageKey.$idx") != null) {
            result.add(prop.getProperty("$messageKey.$idx"))
            idx++
        }
        return result
    }
}