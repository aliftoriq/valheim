package com.example.solvr.ui.plafond

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.solvr.R
import com.example.solvr.models.PlafonDTO

class PlafonAdapter(private val plafonList: List<PlafonDTO.DataItem>) :
    RecyclerView.Adapter<PlafonAdapter.PlafonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlafonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plafon, parent, false)
        return PlafonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlafonViewHolder, position: Int) {
        val plafon = plafonList[position]

        holder.bind(plafon)
    }

    override fun getItemCount(): Int = plafonList.size

    inner class PlafonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titlePackage: TextView = itemView.findViewById(R.id.title_package)
        private val levelText: TextView = itemView.findViewById(R.id.level_text)
        private val rateTenor: TextView = itemView.findViewById(R.id.rate_tenor)
        private val nominal: TextView = itemView.findViewById(R.id.nominal)
        private val bgCard: ImageView = itemView.findViewById(R.id.bg_card)

        fun bind(plafon: PlafonDTO.DataItem) {
            titlePackage.text = plafon.name
            levelText.text = "Level : ${plafon.level}"
            rateTenor.text = "Rate ${plafon.interestRate} - Tenor ${plafon.maxTenorMonths}"
            nominal.text = "Rp. ${plafon.amount}"

            if (plafon.level == 1) {
                bgCard.setImageResource(R.drawable.card_bronze)
            } else if (plafon.level == 2) {
                bgCard.setImageResource(R.drawable.card_silver)
            } else if (plafon.level == 3) {
                bgCard.setImageResource(R.drawable.card_gold)
            } else if (plafon.level == 4) {
                bgCard.setImageResource(R.drawable.card_platinum)
            } else if (plafon.level == 5) {
                bgCard.setImageResource(R.drawable.card_diamond)
            } else {
                bgCard.setImageResource(R.drawable.card)
            }
        }
    }
}
