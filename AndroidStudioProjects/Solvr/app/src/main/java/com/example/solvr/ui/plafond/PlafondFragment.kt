package com.example.solvr.ui.plafond

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solvr.R
import com.example.solvr.models.PlafonDTO
import com.example.solvr.viewmodel.PlafondViewModel
import com.facebook.shimmer.ShimmerFrameLayout

class PlafondFragment : Fragment() {

    private lateinit var plafonViewModel: PlafondViewModel
    private lateinit var plafonRecyclerView: RecyclerView
    private lateinit var plafonAdapter: PlafonAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_plafond, container, false)

        val animTop = AnimationUtils.loadAnimation(inflater.context, R.anim.slide_in_top)
        val animBottom = AnimationUtils.loadAnimation(inflater.context, R.anim.slide_in_bottom)
        val animLeft = AnimationUtils.loadAnimation(inflater.context, R.anim.slide_out_left)
        val animRight = AnimationUtils.loadAnimation(inflater.context, R.anim.slide_in_right)

        root.findViewById<FrameLayout>(R.id.headerContainer).startAnimation(animBottom)

        // Init shimmer AFTER inflating the view
        shimmerLayout = root.findViewById(R.id.shimmerLayout)
        shimmerLayout.startShimmer()

        plafonViewModel = ViewModelProvider(this).get(PlafondViewModel::class.java)

        plafonRecyclerView = root.findViewById(R.id.recyclerPlafon)
        plafonRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        plafonViewModel.plafonList.observe(viewLifecycleOwner, Observer { plafonList ->
            plafonList?.data?.let {
                plafonAdapter = PlafonAdapter(it as List<PlafonDTO.DataItem>)
                plafonRecyclerView.adapter = plafonAdapter

                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                plafonRecyclerView.visibility = View.VISIBLE
            }
            root.findViewById<RecyclerView>(R.id.recyclerPlafon).startAnimation(animBottom)
        })

        plafonViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            // Handle error message
//            shimmerLayout.stopShimmer()
//            shimmerLayout.visibility = View.GONE
//            plafonRecyclerView.visibility = View.VISIBLE

            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

        })

        plafonViewModel.fetchAllPlafon()

        return root
    }
}

