package com.example.budgetapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgetapp.data.TransactionDao

class BudgetViewModelFactory(private val application: Application, private val transactionDao: TransactionDao, private val userId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(application, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 