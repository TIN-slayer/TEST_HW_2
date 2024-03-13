package hse.restaurant.menu.dao

interface MenuDao {
    fun showMenu(): String

    fun takeFood(foodId: Int): Triple<Int, Long, Int>

    fun addFood(name: String, price: Int, time: Int, count: Int)

    fun deleteFood(foodId: Int)
}