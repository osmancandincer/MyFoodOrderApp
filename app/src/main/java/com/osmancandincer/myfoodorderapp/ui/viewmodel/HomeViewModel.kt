package com.osmancandincer.myfoodorderapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.osmancandincer.myfoodorderapp.data.entity.FavoriteFoods
import com.osmancandincer.myfoodorderapp.data.entity.Foods
import com.osmancandincer.myfoodorderapp.data.repo.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(var foodsRepository: FoodsRepository) : ViewModel() {

    var foodsList = MutableLiveData<List<Foods>?>()
    var favoriteFoodsList = MutableLiveData<List<FavoriteFoods>>()

    init {
        loadFoods()
        favoriteFoodsLoad()
    }

    fun loadFoods() {
        CoroutineScope(Dispatchers.Main).launch {
            foodsList.value = foodsRepository.loadFoods()
        }
    }

    fun favoriteFoodsLoad() {
        CoroutineScope(Dispatchers.Main).launch {
            favoriteFoodsList.value = foodsRepository.favoriteFoodsLoad()
        }
    }

    fun addFavoriteFoods(
        food_id: Int,
        food_name: String,
        food_image_name: String,
        food_price: Int
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            foodsRepository.addFavoriteFoods(food_id, food_name, food_image_name, food_price)
        }
    }

    fun deleteFavorite(food_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            foodsRepository.deleteFavorite(food_id)
        }
    }

    fun search(query: String) {//Aramaya gçre yemekleri filtreler.
        val result = foodsList.value?.filter { it.food_name.contains(query, ignoreCase = true) }
        foodsList.value = result
    }

    suspend fun isFavorite(food_id: Int): Int {//Favori durumu kontrol edilir.
        var result = 0
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                result = foodsRepository.isFavorite(food_id)
            }
        }.join()
        return result
    }
}