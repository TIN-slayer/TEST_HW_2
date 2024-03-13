package hse.restaurant.orders.dao

import java.sql.Connection
import java.sql.Statement

class OrderDaoSql(connection: Connection, private val dataLock: Any) : OrderDao {
    private val statement: Statement = connection.createStatement()

    override fun addOrder(): Int {
        synchronized(dataLock) {
            statement.executeUpdate(
                "INSERT INTO sql11690122.Orders (FoodCount, Bill) VALUES (0, 0)"
            )
            val stRes = statement.executeQuery("SELECT MAX(OrderID) FROM sql11690122.Orders")
            stRes.next()
            return stRes.getInt("MAX(OrderID)")
        }
    }

    override fun getBill(orderId: Int): Int {
        synchronized(dataLock) {
            val stRes = statement.executeQuery(
                "SELECT Bill FROM sql11690122.Orders WHERE OrderID = ${orderId}"
            )
            stRes.next()
            return stRes.getInt("Bill")
        }
    }

    override fun addUpBill(orderId: Int, price: Int) {
        synchronized(dataLock) {
            val bill = getBill(orderId) + price
            statement.executeUpdate(
                "UPDATE sql11690122.Orders SET Bill = ${bill} WHERE OrderID = ${orderId}"
            )
        }
    }

    override fun getFoodCount(orderId: Int): Int {
        synchronized(dataLock) {
            val stRes = statement.executeQuery(
                "SELECT FoodCount FROM sql11690122.Orders WHERE OrderID = ${orderId}"
            )
            stRes.next()
            return stRes.getInt("FoodCount")
        }
    }

    override fun changeFoodCount(orderId: Int, addValue: Int) {
        synchronized(dataLock) {
            val foodCount = getFoodCount(orderId) + addValue
//            val stRes = statement.executeQuery(
//                "SELECT FoodCount FROM sql11690122.Orders WHERE OrderID = ${orderId}"
//            )
//            stRes.next()
//            val foodCount = stRes.getInt("FoodCount") + addValue
            statement.executeUpdate(
                "UPDATE sql11690122.Orders SET FoodCount = ${foodCount} WHERE OrderID = ${orderId}"
            )
        }
    }
}
