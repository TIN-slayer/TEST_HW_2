package hse.restaurant.system

import hse.restaurant.menu.exception.MenuException
import hse.restaurant.orders.exception.OrderException
import hse.restaurant.treasury.exception.TreasuryException
import hse.restaurant.users.exception.UserException

// Синглтон
object UserInterface {
    //Добавить смену админа и кастомера

    private val startComs = arrayOf(
        "Зарегистрироваться",
        "Войти",
        "Выйти"
    )

    private val guestComs = arrayOf(
        "Открыть меню",
        "Проверить статус заказа",
        "Отменить заказ",
        "Оплатить заказ",
        "Выйти"
    )

    private val adminComs = arrayOf(
        "Добавить блюдо",
        "Список блюд",
        "Удалить блюдо",
        "Отобразить выручку ресторана",
        "Выйти"
    )

    fun start() {
        try {
            println("Добро пожаловать в наш ресторан!")
            if (SystemManager.setupConnections()) {
                loginMenu()
            }
        } catch (e: Exception) {
            println("Непредвиденная ошибка. Инициация перезапуска программы")
            start()
        }
    }

    private fun loginMenu() {
        var isFinished = false
        var loopContinue = true
        while (loopContinue) {
            try {
                loopContinue = false
                println("Введите номер команды:")
                var i = 1
                startComs.forEach { println("${i++}. $it") }
                val com = readln()
                when (com) {
                    "1" -> SystemManager.signUp() // Зарегистрироваться
                    "2" -> SystemManager.signIn() // Войти
                    "3" -> isFinished = true // Выйти
                    else -> loopContinue = true
                }
            } catch (e: UserException) {
                println(e.message)
                loopContinue = true
            }
        }
        if (isFinished) {
            println("До новых встреч!")
        } else {
            if (!SystemManager.isUserAdmin()) {
                guestMenu()
            } else {
                adminMenu()
            }
        }
    }

    private fun guestMenu() {
        while (true) {
            try {
                println("Введите номер команды:")
                var i = 1
                guestComs.forEach { println("${i++}. $it") }
                val com = readln()
                when (com) {
                    "1" -> SystemManager.guestFoodMenu() // Открыть меню
                    "2" -> SystemManager.orderStatus() // Проверить статус заказа
                    "3" -> SystemManager.cancelOrder() // Отменить заказ
                    "4" -> SystemManager.payBill() // Оплатить заказ
                    "5" -> {
                        loginMenu()
                        break
                    }// Выйти
                }
            } catch (e: UserException) {
                println(e.message)
            } catch (e: OrderException) {
                println(e.message)
            } catch (e: MenuException) {
                println(e.message)
            } catch (e: TreasuryException) {
                println(e.message)
            }
        }
    }

    private fun adminMenu() {
        while (true) {
            try {
                println("Введите номер команды:")
                var i = 1
                adminComs.forEach { println("${i++}. $it") }
                val com = readln()
                when (com) {
                    "1" -> SystemManager.addFoodInMenu() // Добавить блюдо
                    "2" -> SystemManager.showAdminMenu() // Список блюд
                    "3" -> SystemManager.deleteFood() // Удалить блюдо
                    "4" -> SystemManager.showIncome() // Отобразить выручку ресторана
                    "5" -> {
                        loginMenu()
                        break
                    }// Выйти
                }
            } catch (e: UserException) {
                println(e.message)
            } catch (e: OrderException) {
                println(e.message)
            } catch (e: MenuException) {
                println(e.message)
            } catch (e: TreasuryException) {
                println(e.message)
            }
        }
    }
}
