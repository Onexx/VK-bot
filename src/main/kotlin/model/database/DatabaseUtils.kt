package model.database

import org.mariadb.jdbc.MariaDbDataSource
import java.io.IOException
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource

object DatabaseUtils {

    fun getDataSource(): DataSource {
        return DataSourceHolder.INSTANCE!!
    }

    private object DataSourceHolder {
        var INSTANCE: DataSource? = null
        private val properties = Properties()

        init {
            try {
                properties.load(DataSourceHolder::class.java.getResourceAsStream("/application.properties"))
            } catch (e: IOException) {
                throw RuntimeException("Can't load /application.properties.", e)
            }
            try {
                val instance = MariaDbDataSource()
                instance.setUrl(properties.getProperty("database.url"))
                instance.user = properties.getProperty("database.user")
                instance.setPassword(properties.getProperty("database.password"))
                INSTANCE = instance
            } catch (e: SQLException) {
                throw RuntimeException("Can't initialize DataSource.", e)
            }
            try {
                INSTANCE?.connection.use { connection ->
                    if (connection == null) {
                        throw RuntimeException("Can't create testing connection via DataSource.")
                    }
                }
            } catch (e: SQLException) {
                throw RuntimeException("Can't create testing connection via DataSource.", e)
            }
        }
    }
}