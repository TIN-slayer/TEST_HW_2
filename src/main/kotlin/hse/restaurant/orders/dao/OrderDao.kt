package hse.restaurant.orders.dao

interface OrderDao {
    fun addOrder(): Int

    fun getBill(orderId: Int): Int

    fun addUpBill(orderId: Int, price: Int)

    fun getFoodCount(orderId: Int): Int

    fun changeFoodCount(orderId: Int, addValue: Int)
}