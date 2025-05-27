package com.example.budgetapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetapp.R
import com.example.budgetapp.data.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.budgetapp.data.TransactionType

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
        private val categoryText: TextView = itemView.findViewById(R.id.categoryText)
        private val amountText: TextView = itemView.findViewById(R.id.amountText)
        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

        @SuppressLint("SetTextI18n")
        fun bind(transaction: Transaction) {
            descriptionText.text = transaction.description
            categoryText.text = "${transaction.category} • ${dateFormat.format(transaction.date)}"
            
            val amount = transaction.amount
            amountText.text = currencyFormat.format(amount)
            amountText.setTextColor(
                itemView.context.getColor(
                    if (transaction.type == TransactionType.INCOME) 
                        android.R.color.holo_green_dark 
                    else 
                        android.R.color.holo_red_dark
                )
            )
        }
    }

    private class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
} 