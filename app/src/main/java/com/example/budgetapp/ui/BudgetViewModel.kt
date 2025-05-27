package com.example.budgetapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.data.Transaction
import com.example.budgetapp.data.TransactionRepository
import com.example.budgetapp.data.TransactionType
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application, private val userId: Long) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    val userTransactions: LiveData<List<Transaction>>
    val totalIncome: LiveData<Double>
    val totalExpenses: LiveData<Double>

    init {
        val transactionDao = com.example.budgetapp.data.AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        userTransactions = repository.getUserTransactions(userId)
        totalIncome = repository.getUserTotalIncome(userId)
        totalExpenses = repository.getUserTotalExpenses(userId)
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insert(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.update(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }

    fun getTransactionsByType(type: TransactionType): LiveData<List<Transaction>> {
        return repository.getTransactionsByType(type)
    }
} 