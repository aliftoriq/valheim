package com.example.solvr.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.solvr.R
import com.example.solvr.models.LoanDTO

class HistoryAdapter(private var historyList: List<LoanDTO.DataItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    // Update history data without recreating the adapter
    fun updateData(newHistoryList: List<LoanDTO.DataItem>) {
        historyList = newHistoryList
        notifyDataSetChanged() // Notify the RecyclerView that data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loan_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStatus: TextView = itemView.findViewById(R.id.tvLoanStatus)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvLoanAmount)
        private val tvDate: TextView = itemView.findViewById(R.id.tvRequestedDate)
        private val tvTenor: TextView = itemView.findViewById(R.id.tvTenor)

        fun bind(item: LoanDTO.DataItem) {
            tvStatus.text = "Status: ${item.status ?: "-"}"
            tvAmount.text = "Jumlah: Rp. ${item.loanAmount?.toString() ?: "-"}"
            tvDate.text = "Tanggal: ${item.requestedAt?.substring(0,10) ?: "-"}"
            tvTenor.text = "Tenor: ${item.loanTenor ?: 0} bulan"
        }
    }
}
