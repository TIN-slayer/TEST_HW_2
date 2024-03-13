package hse.restaurant.orders.service

import hse.restaurant.orders.dao.OrderDao
import hse.restaurant.orders.exception.OrderException
import java.sql.SQLException
import kotlin.concurrent.thread

class OrderServiceImpl(private val dao: OrderDao) : OrderService {
    override fun makeOrder(): Int {
        try {
            return dao.addOrder()
        } catch (e: SQLException) {
            throw OrderException("Возникли проблемы, попробуйте ещё раз")
        }
    }

    override fun orderFoodItem(orderId: Int, price: Int, time: Long) {
        thread {
            try {
                dao.addUpBill(orderId, price)
                dao.changeFoodCount(orderId, 1)
                Thread.sleep(time)
                dao.changeFoodCount(orderId, -1)
            } catch (e: SQLException) {
                throw OrderException("Возникли проблемы, попробуйте ещё раз")
            }
        }
    }

    override fun getOrderStatus(orderId: Int): Boolean {
        try {
            return dao.getFoodCount(orderId) > 0 // Заказ в процессе
        } catch (e: SQLException) {
            throw OrderException("Возникли проблемы, попробуйте ещё раз")
        }
    }

    override fun getBill(orderId: Int): Int {
        try {
            return dao.getBill(orderId)
        } catch (e: SQLException) {
            throw OrderException("Возникли проблемы, попробуйте ещё раз")
        }
    }
}
