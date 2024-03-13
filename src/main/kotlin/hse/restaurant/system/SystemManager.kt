package hse.restaurant.system

import hse.restaurant.extentions.toBoolean
import hse.restaurant.menu.dao.MenuDaoSql
import hse.restaurant.menu.exception.MenuException
import hse.restaurant.menu.service.MenuService
import hse.restaurant.menu.service.MenuServiceImpl
import hse.restaurant.orders.dao.OrderDaoSql
import hse.restaurant.orders.service.OrderService
import hse.restaurant.orders.service.OrderServiceImpl
import hse.restaurant.treasury.dao.TreasuryDaoSql
import hse.restaurant.treasury.service.TreasuryService
import hse.restaurant.treasury.service.TreasuryServiceImpl
import hse.restaurant.users.dao.UserDaoSql
import hse.restaurant.users.exception.UserException
import hse.restaurant.users.service.UserService
import hse.restaurant.users.service.UserServiceImpl
import java.sql.Connection
import java.sql.DriverManager

object SystemManager {
    private lateinit var connection: Connection
    private lateinit var userService: UserService
    private lateinit var orderService: OrderService
    private lateinit var menuService: MenuService
    private lateinit var treasuryService: TreasuryService
    private var userId = 0

    fun setupConnections(): Boolean {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11690122", "sql11690122", "ajD8b5r9eL"
            )
            val dataLock = Any()
            userService = UserServiceImpl(UserDaoSql(connection, dataLock))
            orderService = OrderServiceImpl(OrderDaoSql(connection, dataLock))
            menuService = MenuServiceImpl(MenuDaoSql(connection, dataLock))
            treasuryService = TreasuryServiceImpl(TreasuryDaoSql(connection, dataLock))
            return true
        } catch (e: Exception) {
            println(
                "Извините, возникла проблема с подключением к серверу, немного подождите и запустите приложение ещё раз. " +
                        "Если не помогает, то скорее всего сервер упал, напишите пж мне в тг @tinslayer"
            )
            return false
        }
    }

    fun signUp() {
        println("Являетесь ли вы админом? Ответ: д/н")
        val isAdmin = readln().toBoolean()
        println("Придумайте и введите логин длины не более 45 символов")
        val userName = readln()
        if (userName.length > 45) {
            throw UserException("Логин должен быть длиной не более 45 символов")
        }
        println("Придумайте и введите пароль длины не более 45 символов")
        val password = readln()
        if (password.length > 45) {
            throw UserException("Пароль должен быть длиной не более 45 символов")
        }
        userId = userService.signUp(userName, password, isAdmin)
        println("Вы успешно зарегистрировались и вошли в систему")
    }

    fun signIn() {
        println("Введите логин длины не более 45 символов")
        val userName = readln()
        if (userName.length > 45) {
            throw UserException("Логин должен быть длиной не более 45 символов")
        }
        println("Введите пароль длины не более 45 символов")
        val password = readln()
        if (password.length > 45) {
            throw UserException("Пароль должен быть длиной не более 45 символов")
        }
        userId = userService.signIn(userName, password)
        println("Вы успешно вошли в систему")
    }

    fun isUserAdmin(): Boolean {
        return userService.getIsAdmin(userId)
    }

    fun guestFoodMenu() {
        var orderId = userService.getOrderId(userId)
        if (orderId != 0 && !orderService.getOrderStatus(orderId)) {
            println("Оплатите текущий заказ прежде чем начать новый")
        } else {
            println(
                "Введите через пробел номера блюд, которые хотите заказать " +
                        "или 0, если хотите вернуться назад:"
            )
            menuService.showMenu()
            val textOrder = readln()
            if (textOrder != "0") {
                val missed = mutableListOf<String>()
                for (foodIdStr in textOrder.split("\\s+".toRegex())) {
                    try {
                        val (price, time, count) = menuService.takeFood(foodIdStr.toInt())
                        if (count >= 0) {
                            if (orderId == 0) {
                                orderId = orderService.makeOrder()
                                userService.assignOrder(userId, orderId)
                            }
                            orderService.orderFoodItem(orderId, price, time * 1000)
                        } else {
                            missed.add(foodIdStr)
                        }
                    } catch (e: NumberFormatException) {
                        throw MenuException("Некорректные номера блюд")
                    }
                    println("Спасибо, блюда успешно добавлены в заказ.")
                    if (missed.size > 0) {
                        println("За исключением тех, что не было в наличии:")
                        missed.forEach { print("${it} ") }
                        println()
                    }
                    Thread.sleep(100)
                    println("Стоимость заказа ${orderService.getBill(orderId)} р")
                }
            }
        }
    }

    fun orderStatus() {
        val orderId = userService.getOrderId(userId)
        if (orderId == 0) {
            println("У вас пока нет активного заказа.")
        } else {
            if (orderService.getOrderStatus(orderId)) {
                println("Заказ в процессе приготовления. Его стоимость ${orderService.getBill(orderId)} р")
            } else {
                println("Заказ готов и ожидает оплаты в размере ${orderService.getBill(orderId)} р")
            }
        }
    }

    fun cancelOrder() {
        val orderId = userService.getOrderId(userId)
        if (orderId == 0) {
            println("У вас пока нет активного заказа.")
        } else {
            if (orderService.getOrderStatus(orderId)) {
                println("Заказ отменён")
                userService.assignOrder(userId, 0)
            } else {
                println("Заказ уже готов и его нельзя отменить")
            }
        }
    }

    fun payBill() {
        val orderId = userService.getOrderId(userId)
        if (orderId == 0) {
            println("У вас пока нет активного заказа.")
        } else {
            if (orderService.getOrderStatus(orderId)) {
                println("Заказ в процессе и его нельзя оплатить")
            } else {
                val bill = orderService.getBill(orderId)
                treasuryService.receivePayment(bill)
                userService.assignOrder(userId, 0)
                println("Заказ оплачен на сумму в размере ${orderService.getBill(orderId)} р")
            }
        }
    }

    fun addFoodInMenu() {
        println("Введите название блюда")
        val name = readln()
        try {
            println("Введите стоимость блюда в рублях")
            val price = readln().toInt()
            println("Введите длительность приготовления блюда в секндах")
            val time = readln().toInt()
            println("Введите кол-во блюд в наличии")
            val count = readln().toInt()
            menuService.addFood(name, price, time, count)
            println("Блюдо успешно добавлено")
        } catch (e: NumberFormatException) {
            throw MenuException("Стоимость, длительность и кол-во должны быть числами")
        }
    }

    fun showAdminMenu() {
        menuService.showMenu()
    }

    fun deleteFood() {
        menuService.showMenu()
        try {
        println("Выберите номер блюда, которое желаете удалить")
            val foodId = readln().toInt()
            menuService.deleteFood(foodId)
            println("Блюдо успешно удалено")
        } catch (e: NumberFormatException) {
            throw MenuException("Некорректный номер блюда")
        }
    }

    fun showIncome() {
        println("Выручка ресторана составляет ${treasuryService.showIncome()} р")
    }
}
