package hse.restaurant.treasury.dao

interface TreasuryDao {
    fun receivePayment(bill: Int)

    fun showIncome() : UInt
}