package com.osmancandincer.myfoodorderapp.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.osmancandincer.myfoodorderapp.R
import com.osmancandincer.myfoodorderapp.data.entity.CartItem
import com.osmancandincer.myfoodorderapp.databinding.FragmentCartBinding
import com.osmancandincer.myfoodorderapp.ui.adapter.CartAdapter
import com.osmancandincer.myfoodorderapp.ui.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private var foodList: List<CartItem> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: CartViewModel by viewModels()
        viewModel = tempViewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        binding.cartFragment = this

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.loadCart("osmancandincer")

        val recyclerView: RecyclerView = binding.cartRecyclerView

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.cartFoodList.observe(viewLifecycleOwner) { cartfoods ->
            cartfoods?.let {
                val cartAdapter = CartAdapter(requireContext(), it, viewModel)
                binding.cartRecyclerView.adapter = cartAdapter
                foodList = it

                if (it.isEmpty()){
                    binding.totalText.visibility = View.GONE
                    binding.orderButton.visibility = View.GONE
                    binding.cartRecyclerView.visibility = View.GONE
                    binding.totalPriceText.visibility = View.GONE
                    binding.emptyAnim.visibility = View.VISIBLE
                    binding.cartEmptyTV.visibility = View.VISIBLE
                }else{
                    binding.emptyAnim.visibility = View.GONE
                    binding.cartEmptyTV.visibility = View.GONE
                    binding.cartRecyclerView.visibility = View.VISIBLE

                }
            }
        }
        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.totalPriceText.text = "₺${it}"
        }

    }

    fun orderButton() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Siparişi onaylıyor musunuz?")
        builder.setPositiveButton("Evet") { dialog, which ->
            dialog.dismiss()
            showCAnimationDialog()
            deleteFoodFromCart()

        }
        builder.setNegativeButton("Hayır") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun deleteFoodFromCart() {
        foodList.forEach { viewModel.deleteFoodFromCart(it.cart_food_id, it.username) }

        viewModel.loadCart("osmancandincer")

    }

    private fun showCAnimationDialog() {
        //Siparişin başarıyla tamamlandığını gösteren bir animasyonlu bir dialog gösterir.
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.order_anim)
        dialog.setCanceledOnTouchOutside(false)
        val animationView = dialog.findViewById<LottieAnimationView>(R.id.animationView)
        val json =
            resources.openRawResource(R.raw.order_anim).bufferedReader().use { it.readText() }
        LottieCompositionFactory.fromJsonString(json, "order_anim.json")
            .addListener { lottieResult ->
                lottieResult?.let { composition ->
                    animationView.setComposition(composition)
                    animationView.speed = (composition.duration / 2000f)
                    animationView.playAnimation()
                    dialog.show()
                    animationView.postDelayed({
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }, 2000)
                }
            }
    }
}

