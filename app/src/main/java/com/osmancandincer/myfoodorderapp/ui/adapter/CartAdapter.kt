package com.osmancandincer.myfoodorderapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.osmancandincer.myfoodorderapp.R
import com.osmancandincer.myfoodorderapp.data.entity.CartItem
import com.osmancandincer.myfoodorderapp.databinding.ItemCartBinding
import com.osmancandincer.myfoodorderapp.ui.viewmodel.CartViewModel

class CartAdapter(
    var mContext: Context, var cartFoodList: List<CartItem>, var viewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartItemHolder>() {

    inner class CartItemHolder(var binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemHolder {
        val binding: ItemCartBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_cart,
            parent,
            false
        )
        return CartItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemHolder, position: Int) {

        val cart = cartFoodList.get(position)
        val t = holder.binding

        t.apply {
            foodNameTextView.text = cart.food_name
            foodPriceTextView.text = "${cart.food_price} ₺"
            foodPriceTotalText.text = "${cart.getTotalPrice()} ₺"
            orderAmountText.text = cart.food_order_quantity.toString()
        }
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${cart.food_image_name}"
        Glide.with(mContext).load(url).into(t.imageViewFood)

        t.minusButton.setOnClickListener {
            if (cart.food_order_quantity > 1) {
                cart.food_order_quantity--
                updateUI(cart, t)
            }
        }

        t.plusButton.setOnClickListener {
            cart.food_order_quantity++
            updateUI(cart, t)
        }

        t.closeButton.setOnClickListener {
            val message = "${cart.food_name} sepetten kaldırılsın mı?"
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).setAction("Evet") {

                viewModel.deleteFoodFromCart(cart.cart_food_id, cart.username)
                viewModel.loadCart("osmancandincer")
            }.show()
        }
    }

    override fun getItemCount(): Int {
        return cartFoodList.size
        //Adapterdeki öğe sayısı
    }

    private fun updateUI(cart: CartItem, t: ItemCartBinding) {
        t.orderAmountText.text = cart.food_order_quantity.toString()
        t.foodPriceTotalText.text = "${cart.getTotalPrice()} ₺"
        viewModel.totalPrice()
        viewModel.updateCart(cart)
        //sepetin toplam fiyatı güncellenir ve sepetin güncellenmiş hali ViewModel'e iletilir.
    }

    fun CartItem.getTotalPrice(): Int {
        return food_price * food_order_quantity
        //toplam fiyat hesaplanır.
    }
}