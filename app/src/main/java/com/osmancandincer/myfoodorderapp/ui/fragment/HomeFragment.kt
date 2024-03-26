package com.osmancandincer.myfoodorderapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.osmancandincer.myfoodorderapp.R
import com.osmancandincer.myfoodorderapp.databinding.FragmentHomeBinding
import com.osmancandincer.myfoodorderapp.ui.adapter.FoodsAdapter
import com.osmancandincer.myfoodorderapp.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomeViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeFragment = this

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recylerView.layoutManager = layoutManager


        viewModel.foodsList.observe(viewLifecycleOwner) { foods ->
            foods?.let {
                val foodsListAdapter = FoodsAdapter(requireContext(), it, viewModel)
                binding.recylerView.adapter = foodsListAdapter
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query)
                return true
            }
        })

        binding.searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                viewModel.loadFoods()
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFoods()
    }
}