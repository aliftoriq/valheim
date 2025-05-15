package com.example.yourapp.utils

import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.example.solvr.R
import android.content.Context
import android.widget.LinearLayout

class SwitchAllertCustom(private val context: Context) {

    fun show(
        message: String,
        onYes: () -> Unit,
        onNo: () -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_switch_allert, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.show()
        dialog.window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.85).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val messageTextView = dialogView.findViewById<TextView>(R.id.messageTextView)
        val yesButton = dialogView.findViewById<Button>(R.id.positiveButton)
        val noButton = dialogView.findViewById<Button>(R.id.negativeButton)

        messageTextView.text = message

        yesButton.setOnClickListener {
            onYes()
            dialog.dismiss()
        }

        noButton.setOnClickListener {
            onNo()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}
