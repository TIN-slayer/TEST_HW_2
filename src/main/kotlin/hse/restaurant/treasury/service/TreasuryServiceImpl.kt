package hse.restaurant.treasury.service

import hse.restaurant.treasury.dao.TreasuryDao
import hse.restaurant.treasury.exception.TreasuryException
import java.sql.SQLException

class TreasuryServiceImpl(private val dao: TreasuryDao) : TreasuryService {
    override fun receivePayment(bill: Int) {
        try {
            dao.receivePayment(bill)
        } catch (e: SQLException) {
            throw TreasuryException("Возникли проблемы, попробуйте ещё раз")
        }
    }

    override fun showIncome(): UInt {
        try {
            return dao.showIncome()
        } catch (e: SQLException) {
            throw TreasuryException("Возникли проблемы, попробуйте ещё раз")
        }
    }
}
