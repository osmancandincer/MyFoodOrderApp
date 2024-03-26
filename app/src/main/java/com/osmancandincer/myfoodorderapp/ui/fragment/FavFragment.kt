package com.osmancandincer.myfoodorderapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.osmancandincer.myfoodorderapp.R
import com.osmancandincer.myfoodorderapp.databinding.FragmentFavBinding
import com.osmancandincer.myfoodorderapp.ui.adapter.FavoriteFoodsAdapter
import com.osmancandincer.myfoodorderapp.ui.viewmodel.FavViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class FavFragment : Fragment() {

    private lateinit var binding: FragmentFavBinding
    private lateinit var viewModel: FavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FavViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_fav, container, false)

        viewModel.favoriteFoodList.observe(viewLifecycleOwner) {
            val favoriteFoodsListAdapter = FavoriteFoodsAdapter(requireContext(), it, viewModel)
            binding.favAdapter = favoriteFoodsListAdapter
        }
        return binding.root
    }

}