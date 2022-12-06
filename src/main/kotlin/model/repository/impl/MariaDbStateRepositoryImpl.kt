package model.repository.impl

import model.database.DatabaseUtils
import model.domain.DialogState
import model.domain.DialogState.valueOf
import model.repository.StateRepository
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import javax.sql.DataSource

class MariaDbStateRepositoryImpl : StateRepository {
    private val dataSource: DataSource = DatabaseUtils.getDataSource()
    override fun save(userId: Int, state: DialogState) {
        try {
            dataSource.connection.use { connection ->
                connection.prepareStatement(
                    "REPLACE INTO `State` (`userId`, `state`) VALUES (?, ?)"
                ).use { statement ->
                    statement.setInt(1, userId)
                    statement.setString(2, state.name)

                    if (statement.executeUpdate() != 1) {
                        System.err.println("Couldn't save state for user $userId: Update unsuccessful")
                    }
                }
            }
        } catch (e: SQLException) {
            System.err.println("Couldn't save state for user $userId: $e")
        }
    }

    override fun find(userId: Int): DialogState? {
        try {
            dataSource.connection.use { connection ->
                connection.prepareStatement("SELECT * FROM State WHERE userId=?").use { statement ->
                    statement.setInt(1, userId)
                    return statement.executeQuery().use { resultSet ->
                        toDialogState(statement.metaData, resultSet)
                    }
                }
            }
        } catch (e: SQLException) {
            System.err.println("Couldn't find state for user $userId: $e")
            return null
        }
    }

    @Throws(SQLException::class)
    private fun toDialogState(metaData: ResultSetMetaData, resultSet: ResultSet): DialogState? {
        if (!resultSet.next()) {
            return null
        }
        for (i in 1..metaData.columnCount) {
            when (metaData.getColumnName(i)) {
                "state" -> return valueOf(resultSet.getString(i))
                else -> {}
            }
        }
        return null
    }
}