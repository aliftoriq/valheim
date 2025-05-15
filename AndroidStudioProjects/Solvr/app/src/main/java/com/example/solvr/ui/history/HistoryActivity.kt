package com.example.solvr.ui.history

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solvr.R

class HistoryActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_history)
//    }

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.historyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = HistoryAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.fetchHistory()

        viewModel.loanHistory.observe(this) { history ->
            if (history.isNotEmpty()) {
                adapter.updateData(history)
            } else {
                showError("No loan history available")
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            showError(errorMessage)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
