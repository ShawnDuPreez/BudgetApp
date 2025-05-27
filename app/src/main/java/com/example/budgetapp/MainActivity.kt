package com.example.budgetapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetapp.data.AppDatabase
import com.example.budgetapp.data.Transaction
import com.example.budgetapp.data.TransactionType
import com.example.budgetapp.ui.BudgetViewModel
import com.example.budgetapp.ui.BudgetViewModelFactory
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
    private lateinit var sharedPreferences: SharedPreferences
    private var currentUserId: Long = -1
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Check if user is logged in
        val username = sharedPreferences.getString("username", null)
        val userId = sharedPreferences.getLong("loggedInUserId", -1)

        if (username == null || userId == -1L) {
            // If not logged in, redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return // Stop further execution of onCreate
        }

        currentUserId = userId

        // User is logged in, proceed with main activity setup
        setContentView(R.layout.activity_main)

        // Initialize views
        totalBalanceText = findViewById(R.id.totalBalanceText)
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView)
        fab = findViewById(R.id.fab)
        btnLogout = findViewById(R.id.btnLogout)

        // Setup RecyclerView
        adapter = TransactionAdapter()
        transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionsRecyclerView.adapter = adapter

        // Initialize ViewModel with the current user ID
        val database = AppDatabase.getDatabase(this)
        val viewModelFactory = BudgetViewModelFactory(application, database.transactionDao(), currentUserId)
        viewModel = ViewModelProvider(this, viewModelFactory)[BudgetViewModel::class.java]

        // Observe transactions
        viewModel.userTransactions.observe(this) { transactions ->
            adapter.submitList(transactions)
        }

        // Observe total balance - This will need to be updated to calculate balance per user
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

        // Setup Logout button click listener
        btnLogout.setOnClickListener {
            // Clear logged-in user data
            with(sharedPreferences.edit()) {
                remove("loggedInUserId")
                apply()
            }

            // Navigate back to the landing screen
            val intent = Intent(this, LandingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
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

                if (currentUserId != -1L) {
                    val transaction = Transaction(
                        userId = currentUserId,
                        amount = amount,
                        description = description,
                        category = category,
                        type = type,
                        date = Date()
                    )

                    viewModel.addTransaction(transaction)
                } else {
                    Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amount)
    }
} 