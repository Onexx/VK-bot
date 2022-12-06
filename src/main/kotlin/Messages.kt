import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

object Messages {
    private val prop = Properties()

    init {
//        val file = File("src/main/resources/messages.ru")

//        System.err.println(Paths.get("").toAbsolutePath())
//        System.err.println(file.absolutePath)
        try {
            val resourceStream: InputStream? = this.javaClass.classLoader.getResourceAsStream("messages.ru")
            if (resourceStream == null) {
                System.err.println("Couldn't load messages")
            }
            val a = System.getenv()
            resourceStream?.use {
                prop.load(it)
            }

//            FileInputStream(file).use {
//                prop.load(it)
//            }
        } catch (e: FileNotFoundException) {
            System.err.println("Couldn't load config'")
        }
    }

    fun getMessage(messageKey: String): String {
        return prop.getProperty(messageKey) ?: messageKey
    }
}