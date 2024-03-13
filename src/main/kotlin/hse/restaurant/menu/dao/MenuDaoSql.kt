package hse.restaurant.menu.dao

import hse.restaurant.extentions.toInt
import java.sql.Connection
import java.sql.Statement

class MenuDaoSql(connection: Connection, private val dataLock: Any) : MenuDao {
    private val statement: Statement = connection.createStatement()
    override fun showMenu(): String {
        synchronized(dataLock) {
            val stRes = statement.executeQuery("SELECT * FROM sql11690122.Menu")
            var res = ""
            while (stRes.next()) {
                res += "${stRes.getInt("FoodID")}. ${stRes.getString("Name")} - " +
                        "Цена: ${stRes.getInt("Price")} р, " +
                        "Время приготовления: ${stRes.getInt("Time")} с, " +
                        "Осталось: ${stRes.getInt("Count")} шт.\n"
            }
            return res
        }
    }

    override fun takeFood(foodId: Int): Triple<Int, Long, Int> {
        synchronized(dataLock) {
            val stRes =
                statement.executeQuery("SELECT * FROM sql11690122.Menu WHERE FoodID = ${foodId}")
            stRes.next()
            val count = stRes.getInt("Count") - 1
            val price = stRes.getInt("Price")
            val time = stRes.getInt("Time").toLong()
            if (count >= 0) {
                statement.executeUpdate(
                    "UPDATE sql11690122.Menu SET Count = ${count} WHERE FoodID = ${foodId}"
                )
            }
            return Triple(price, time, count)
        }
    }

    override fun addFood(name: String, price: Int, time: Int, count: Int) {
        synchronized(dataLock) {
            statement.executeUpdate(
                "INSERT INTO sql11690122.Menu (Name, Price, Time, Count) VALUES ('${name}', ${price}, ${time}, ${count})"
            )
        }
    }

    override fun deleteFood(foodId: Int) {
        synchronized(dataLock) {
            statement.executeUpdate(
                "DELETE FROM sql11690122.Menu WHERE FoodID = ${foodId}"
            )
        }
    }
}
