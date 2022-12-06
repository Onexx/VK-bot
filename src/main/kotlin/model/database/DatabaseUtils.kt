package model.database

import org.mariadb.jdbc.MariaDbDataSource
import java.sql.SQLException
import javax.sql.DataSource

object DatabaseUtils {

    fun getDataSource(): DataSource {
        if (DataSourceHolder.INSTANCE == null) throw RuntimeException("Couldn't initialize DataSource")
        return DataSourceHolder.INSTANCE!!
    }

    private object DataSourceHolder {
        var INSTANCE: DataSource? = null

        init {
            try {
                val instance = MariaDbDataSource()
                instance.setUrl(System.getenv("DATABASE_URL"))
                instance.user = System.getenv("DATABASE_USER")
                instance.setPassword(System.getenv("DATABASE_PASSWORD"))
                INSTANCE = instance
            } catch (e: SQLException) {
                throw RuntimeException("Can't initialize DataSource.", e)
            }
            try {
                INSTANCE?.connection.use { connection ->
                    if (connection == null) {
                        throw RuntimeException("Couldn't create connection via DataSource. Instance is null")
                    }
                }
            } catch (e: SQLException) {
                throw RuntimeException("Couldn't create testing connection via DataSource.", e)
            }
        }
    }
}