package hse.restaurant.users.dao

import hse.restaurant.extentions.toBoolean
import hse.restaurant.extentions.toInt
import java.sql.Connection
import java.sql.Statement
import kotlin.concurrent.thread

class UserDaoSql(connection: Connection, private val dataLock: Any) : UserDao {
    private val statement: Statement = connection.createStatement()

    override fun addUser(userName: String, password: String, isAdmin: Boolean): Int {
        synchronized(dataLock) {
            statement.executeUpdate(
                "INSERT INTO sql11690122.Users (Username, Password, IsAdmin) VALUES ('${userName}', '${password}', ${isAdmin.toInt()})"
            )
            val stRes = statement.executeQuery("SELECT MAX(UserID) FROM sql11690122.Users")
            stRes.next()
            return stRes.getInt("MAX(UserID)")
        }
    }

    override fun verifyUser(userName: String, password: String): Pair<Boolean, Int> {
        synchronized(dataLock) {
            val stRes =
                statement.executeQuery("SELECT UserID FROM sql11690122.Users WHERE Username = '${userName}' AND Password = '${password}'")
            if (!stRes.isBeforeFirst) {
                return Pair(false, 0)
            }
            stRes.next()
            return Pair(true, stRes.getInt("UserID"))
        }
    }

    override fun getIsAdmin(userId: Int): Boolean {
        synchronized(dataLock) {
            val stRes =
                statement.executeQuery("SELECT IsAdmin FROM sql11690122.Users WHERE UserID = ${userId}")
            stRes.next()
            return stRes.getInt("IsAdmin").toBoolean()
        }
    }

    override fun getOrderId(userId: Int): Int {
        synchronized(dataLock) {
            val stRes =
                statement.executeQuery("SELECT OrderID FROM sql11690122.Users WHERE UserID = ${userId}")
            stRes.next()
            return stRes.getInt("OrderID")
        }
    }

    override fun orderFood() {
        thread {
            synchronized(dataLock) {
                val stRes = statement.executeQuery(
                    "SELECT FoodCount FROM sql11690122.Users WHERE UserID = 1"
                )
                stRes.next()
                val foodCount = stRes.getInt("FoodCount") + 1
                statement.executeUpdate(
                    "UPDATE sql11690122.Users\n" + "SET FoodCount = ${foodCount}\n" + "WHERE UserID = 1"
                )
            }
            Thread.sleep(2000)
            synchronized(dataLock) {
                val stRes = statement.executeQuery(
                    "SELECT FoodCount FROM sql11690122.Users WHERE UserID = 1"
                )
                stRes.next()
                val foodCount = stRes.getInt("FoodCount") - 1
                statement.executeUpdate(
                    "UPDATE sql11690122.Users\n" + "SET FoodCount = ${foodCount}\n" + "WHERE UserID = 1"
                )
            }
        }
    }

    override fun orderStatus() {
        synchronized(dataLock) {
            val stRes = statement.executeQuery(
                "SELECT FoodCount FROM sql11690122.Users WHERE UserID = 1"
            )
            stRes.next()
            val foodCount = stRes.getInt("FoodCount")
            if (foodCount == 0) {

                println("Заказ готов")
            } else {
                println("Заказ готовится, осталось ${foodCount} блюд")
            }
        }
    }

    override fun assignOrder(userId: Int, orderId: Int) {
        synchronized(dataLock) {
            statement.executeUpdate(
                "UPDATE sql11690122.Users SET OrderID = ${orderId} WHERE UserID = ${userId}"
            )
        }
    }
}
