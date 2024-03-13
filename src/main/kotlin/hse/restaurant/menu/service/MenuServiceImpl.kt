package hse.restaurant.menu.service

import hse.restaurant.menu.dao.MenuDao
import hse.restaurant.menu.exception.MenuException
import hse.restaurant.users.exception.UserException
import java.sql.SQLException

class MenuServiceImpl(private val dao: MenuDao) : MenuService {
    override fun showMenu(){
        try {
            println(dao.showMenu())
        } catch (e: SQLException) {
            throw MenuException("Проблемы с сервером, попробуйте ещё раз")
        }
    }

    override fun takeFood(foodId: Int): Triple<Int, Long, Int> {
        try {
            return dao.takeFood(foodId)
        } catch (e: SQLException) {
            throw MenuException("Нет блюда с таким номером")
        }
    }

    override fun addFood(name: String, price: Int, time: Int, count: Int) {
        try {
            dao.addFood(name, price, time, count)
        } catch (e: SQLException) {
            throw MenuException("Блюдо с таким названием уже есть, попробуйте другое")
        }
    }

    override fun deleteFood(foodId: Int) {
        try {
            return dao.deleteFood(foodId)
        } catch (e: SQLException) {
            throw MenuException("Проблемы с сервером, попробуйте ещё раз")
        }
    }
}