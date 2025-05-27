package com.example.budgetapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.data.BudgetDatabase
import com.example.budgetapp.data.Transaction
import com.example.budgetapp.data.TransactionRepository
import com.example.budgetapp.data.TransactionType
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    val allTransactions: LiveData<List<Transaction>>
    val totalIncome: LiveData<Double>
    val totalExpenses: LiveData<Double>

    init {
        val transactionDao = BudgetDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        allTransactions = repository.allTransactions
        totalIncome = repository.getTotalByType(TransactionType.INCOME)
        totalExpenses = repository.getTotalByType(TransactionType.EXPENSE)
    }

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
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