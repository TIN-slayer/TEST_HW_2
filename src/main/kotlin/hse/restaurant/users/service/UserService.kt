package hse.restaurant.users.service

interface UserService {
    fun signUp(userName: String, password: String, isAdmin: Boolean): Int

    fun signIn(userName: String, password: String): Int

    fun getIsAdmin(userId: Int): Boolean

    fun getOrderId(userId: Int): Int

    fun assignOrder(userId: Int, orderId: Int)
}