package hse.restaurant.treasury.service

interface TreasuryService {
    fun receivePayment(bill: Int)

    fun showIncome(): UInt
}