package hse.restaurant.users.service

import hse.restaurant.users.dao.UserDao
import hse.restaurant.users.exception.UserException
import java.sql.SQLException


class UserServiceImpl(private val dao: UserDao) : UserService {
    override fun signUp(userName: String, password: String, isAdmin: Boolean): Int {
        try {
            return dao.addUser(userName, password, isAdmin)
        } catch (e: SQLException) {
            throw UserException("Данный логин уже занят, попробуйте другой")
        }
    }

    override fun signIn(userName: String, password: String): Int {
        try {
            val (valid, id) = dao.verifyUser(userName, password)
            if (!valid) {
                throw UserException("Неправильный логин или пароль")
            }
            return id
        } catch (e: SQLException) {
            throw UserException("Проблемы с сервером, попробуйте ещё раз")
        }
    }

    override fun getIsAdmin(userId: Int): Boolean {
        try {
            return dao.getIsAdmin(userId)
        } catch (e: SQLException) {
            throw UserException("Проблемы с сервером, попробуйте ещё раз")
        }
    }

    override fun getOrderId(userId: Int): Int {
        try {
            return dao.getOrderId(userId)
        } catch (e: SQLException) {
            throw UserException("Проблемы с сервером, попробуйте ещё раз")
        }
    }

    override fun assignOrder(userId: Int, orderId: Int) {
        try {
            dao.assignOrder(userId, orderId)
        } catch (e: SQLException) {
            throw UserException("Проблемы с сервером, попробуйте ещё раз")
        }
    }
}
