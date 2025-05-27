package com.example.budgetapp

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetapp.data.Transaction
import com.example.budgetapp.data.TransactionType
import com.example.budgetapp.ui.BudgetViewModel
import com.example.budgetapp.ui.TransactionAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: BudgetViewModel
    private lateinit var totalBalanceText: TextView
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        totalBalanceText = findViewById(R.id.totalBalanceText)
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView)
        fab = findViewById(R.id.fab)

        // Setup RecyclerView
        adapter = TransactionAdapter()
        transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsRecyclerView.adapter = adapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        // Observe transactions
        viewModel.allTransactions.observe(this) { transactions ->
            adapter.submitList(transactions)
        }

        // Observe total balance
        viewModel.totalIncome.observe(this) { income ->
            viewModel.totalExpenses.observe(this) { expenses ->
                val balance = (income ?: 0.0) - (expenses ?: 0.0)
                totalBalanceText.text = formatCurrency(balance)
            }
        }

        // Setup FAB click listener
        fab.setOnClickListener {
            showAddTransactionDialog()
        }
    }

    private fun showAddTransactionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_transaction, null)
        val amountEditText = dialogView.findViewById<TextInputEditText>(R.id.amountEditText)
        val descriptionEditText = dialogView.findViewById<TextInputEditText>(R.id.descriptionEditText)
        val categoryEditText = dialogView.findViewById<TextInputEditText>(R.id.categoryEditText)
        val incomeRadioButton = dialogView.findViewById<RadioButton>(R.id.incomeRadioButton)

        AlertDialog.Builder(this)
            .setTitle("Add Transaction")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
                val description = descriptionEditText.text.toString()
                val category = categoryEditText.text.toString()
                val type = if (incomeRadioButton.isChecked) TransactionType.INCOME else TransactionType.EXPENSE

                val transaction = Transaction(
                    amount = amount,
                    description = description,
                    category = category,
                    type = type,
                    date = Date()
                )

                viewModel.addTransaction(transaction)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amount)
    }
} 