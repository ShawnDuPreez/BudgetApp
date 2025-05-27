package com.example.budgetapp.data

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
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
}