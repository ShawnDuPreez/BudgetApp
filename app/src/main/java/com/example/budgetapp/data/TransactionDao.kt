package com.example.budgetapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: TransactionType): LiveData<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    fun getTotalByType(type: TransactionType): LiveData<Double>
} 