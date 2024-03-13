package hse.restaurant.orders.service

interface OrderService {
    fun makeOrder(): Int

    fun orderFoodItem(orderId: Int, price: Int, time: Long)

    fun getOrderStatus(orderId: Int): Boolean

    fun getBill(orderId: Int): Int
}
