package com.osmancandincer.myfoodorderapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.osmancandincer.myfoodorderapp.R
import com.osmancandincer.myfoodorderapp.databinding.FragmentDetailBinding
import com.osmancandincer.myfoodorderapp.ui.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var booleanFavState: Boolean = false
    private var count = 1
    private var totalPrice = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: DetailViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.foodDetailsFragment = this
        binding.foodCount = count

        val bundle: DetailFragmentArgs by navArgs()
        val receivedFood = bundle.food
        binding.foodObject = receivedFood

        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${receivedFood.food_image_name}"
        Glide.with(this).load(url).into(binding.foodImage)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.favState.value = viewModel.isFavorite(receivedFood.food_id).toString()
        }

        viewModel.favState.observe(viewLifecycleOwner) {
            if (viewModel.favState.value!!.toInt() > 0) {
                binding.favImageView.setImageResource(R.drawable.ic_favorite)

            } else {
                binding.favImageView.setImageResource(R.drawable.ic_unfavorite)

            }
        }

        binding.exitImageView.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToHomeFragment()
            findNavController().navigate(action)
        }

    }

    fun onAddToCartButtonClicked(//Sepete ekle butonuna tıklandığında çağrılır.
        food_name: String,
        food_image_name: String,
        food_price: Int,
        food_order_quantity: Int,
        username: String
    ) {
        viewModel.addToCart(food_name,
            food_image_name,
            food_price,
            food_order_quantity,
            username)
        val action = DetailFragmentDirections.actionDetailFragmentToCartFragment()
        findNavController().navigate(action)
    }

    fun buttonMinus() {//Miktarı azaltma
        if (count > 1) {
            count--
            binding.foodCount = count
            totalPrice()
        }
    }

    fun buttonPlus() {//Miktarı arttırma
        count++
        binding.foodCount = count
        totalPrice()
    }

    fun totalPrice() {
        totalPrice = count * binding.foodObject!!.food_price
        binding.totalPriceText.text = "${totalPrice} ₺"
    }


    fun addFavoriteFoods(
        //Favoriye ekle butonuna tıklandığında çağrılır.
        // Sonra favori simgesinin görünümünü günceller.
        food_id: Int,
        food_name: String,
        food_image_name: String,
        food_price: Int
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val isFavorite = viewModel.isFavorite(food_id)
            if (isFavorite == 1) {
                Toast.makeText(context, "Yemek favorilere eklenmiş durumda", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.addFavoriteFoods(food_id, food_name, food_image_name, food_price)
                binding.favImageView.setImageResource(R.drawable.ic_favorite)
                booleanFavState = true
            }
        }
    }
}