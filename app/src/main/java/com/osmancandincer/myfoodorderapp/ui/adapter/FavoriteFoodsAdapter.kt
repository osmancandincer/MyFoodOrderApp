package com.osmancandincer.myfoodorderapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.osmancandincer.myfoodorderapp.R
import com.osmancandincer.myfoodorderapp.data.entity.FavoriteFoods
import com.osmancandincer.myfoodorderapp.databinding.ItemFavoriteBinding
import com.osmancandincer.myfoodorderapp.ui.viewmodel.FavViewModel

class FavoriteFoodsAdapter(
    var mContext: Context,
    var favoriteFoodsList: List<FavoriteFoods>,
    var viewModel: FavViewModel
) : RecyclerView.Adapter<FavoriteFoodsAdapter.FavoriteFoodsHolder>() {

    inner class FavoriteFoodsHolder(var binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteFoodsHolder {
        val binding: ItemFavoriteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_favorite,
            parent,
            false
        )
        return FavoriteFoodsHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteFoodsHolder, position: Int) {
        val favFoods = favoriteFoodsList[position]
        val t = holder.binding

        t.favFoodsObject = favFoods

        t.favButton.setImageResource(R.drawable.ic_favorite)
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${favFoods.food_image}"
        Glide.with(mContext).load(url).into(t.imageViewFood)


        t.favButton.setOnClickListener {
            viewModel.deleteFavorite(favFoods.food_id)
        }
        t.favDelete.setOnClickListener {
            viewModel.deleteFavorite(favFoods.food_id)
        }
    }

    override fun getItemCount(): Int {
        return favoriteFoodsList.size
    }
}