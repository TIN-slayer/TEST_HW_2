package hse.restaurant.users.dao

interface UserDao {
    fun addUser(userName: String, password: String, isAdmin: Boolean): Int

    fun verifyUser(userName: String, password: String): Pair<Boolean, Int>

    fun getIsAdmin(userId: Int): Boolean

    fun getOrderId(userId: Int): Int

    fun orderFood()

    fun orderStatus()

    fun assignOrder(userId: Int, orderId: Int)
}