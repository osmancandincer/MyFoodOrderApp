package com.osmancandincer.myfoodorderapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.osmancandincer.myfoodorderapp.R
import com.osmancandincer.myfoodorderapp.data.entity.Foods
import com.osmancandincer.myfoodorderapp.databinding.ItemFoodsBinding
import com.osmancandincer.myfoodorderapp.ui.fragment.HomeFragmentDirections
import com.osmancandincer.myfoodorderapp.ui.viewmodel.HomeViewModel
import com.osmancandincer.myfoodorderapp.util.change
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodsAdapter(
    var mContext: Context,
    var foodsList: List<Foods>,
    var viewModel: HomeViewModel
) : RecyclerView.Adapter<FoodsAdapter.FoodsViewHolder>() {

    inner class FoodsViewHolder(var binding: ItemFoodsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsViewHolder {
        val binding: ItemFoodsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_foods,
            parent,
            false
        )
        return FoodsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodsViewHolder, position: Int) {
        val food = foodsList.get(position)
        val t = holder.binding
        var fav = false

        t.foodsObject = food

        CoroutineScope(Dispatchers.Main).launch {
            val favoriteCount = getFavoriteCount(food.food_id)
            val isFavorite = favoriteCount > 0
            if (isFavorite) {
                t.favButton.setImageResource(R.drawable.ic_favorite)
            } else {
                t.favButton.setImageResource(R.drawable.ic_unfavorite)
            }
        }

        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${food.food_image_name}"
        Glide.with(mContext).load(url).into(t.foodImage)

        t.foodItemCardView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(food)
            Navigation.change(it, action)
        }

        t.favButton.setOnClickListener {
            fav = !fav
            if (fav == false) {
                t.favButton.setImageResource(R.drawable.ic_unfavorite)
                viewModel.deleteFavorite(food.food_id)
            } else {
                viewModel.addFavoriteFoods(
                    food.food_id,
                    food.food_name,
                    food.food_image_name,
                    food.food_price
                )
                t.favButton.setImageResource(R.drawable.ic_favorite)
            }
        }
    }

    override fun getItemCount(): Int {
        return foodsList.size
    }

    suspend fun getFavoriteCount(food_id: Int): Int {
        return viewModel.isFavorite(food_id)
    }
}