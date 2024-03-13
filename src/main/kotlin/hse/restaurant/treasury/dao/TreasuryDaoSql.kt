package hse.restaurant.treasury.dao

import java.sql.Connection
import java.sql.Statement

class TreasuryDaoSql(connection: Connection, private val dataLock: Any) : TreasuryDao {
    private val statement: Statement = connection.createStatement()

    override fun receivePayment(bill: Int) {
        synchronized(dataLock) {
            val stRes = statement.executeQuery(
                "SELECT Budget FROM sql11690122.Treasury WHERE BudgetID = 1"
            )
            stRes.next()
            val budget = stRes.getInt("Budget") + bill
            statement.executeUpdate(
                "UPDATE sql11690122.Treasury SET Budget = ${budget} WHERE BudgetID = 1"
            )
        }
    }

    override fun showIncome() : UInt{
        val stRes = statement.executeQuery(
            "SELECT Budget FROM sql11690122.Treasury WHERE BudgetID = 1"
        )
        stRes.next()
        return stRes.getInt("Budget").toUInt()
    }
}
