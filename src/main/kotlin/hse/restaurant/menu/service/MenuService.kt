package hse.restaurant.menu.service

interface MenuService {
    fun showMenu()

    fun takeFood(foodId: Int): Triple<Int, Long, Int>

    fun addFood(name: String, price: Int, time: Int, count: Int)

    fun deleteFood(foodId: Int)
}