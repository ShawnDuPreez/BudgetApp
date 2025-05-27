package com.example.budgetapp.data

import androidx.lifecycle.LiveData
import com.example.budgetapp.data.Transaction
import com.example.budgetapp.data.TransactionDao
import com.example.budgetapp.data.TransactionType

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    fun getTransactionsByType(type: TransactionType): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsByType(type)
    }

    fun getTotalByType(type: TransactionType): LiveData<Double> {
        return transactionDao.getTotalByType(type)
    }

    fun getUserTransactions(userId: Long): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsForUser(userId)
    }

    fun getUserTotalIncome(userId: Long): LiveData<Double> {
        return transactionDao.getTotalAmountByTypeForUser(userId, TransactionType.INCOME)
    }

    fun getUserTotalExpenses(userId: Long): LiveData<Double> {
        return transactionDao.getTotalAmountByTypeForUser(userId, TransactionType.EXPENSE)
    }
}