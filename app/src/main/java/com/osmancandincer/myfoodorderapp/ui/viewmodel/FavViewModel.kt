package com.osmancandincer.myfoodorderapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.osmancandincer.myfoodorderapp.data.entity.FavoriteFoods
import com.osmancandincer.myfoodorderapp.data.repo.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(var foodsRepository: FoodsRepository) : ViewModel() {

    var favoriteFoodList = MutableLiveData<List<FavoriteFoods>>()

    init {
        favoriteFoodsLoad()//Başlangıçta favorileri yükler.
    }

    fun favoriteFoodsLoad() {//Favori yemekleri yükler.
        CoroutineScope(Dispatchers.Main).launch {
            favoriteFoodList.value = foodsRepository.favoriteFoodsLoad()
        }
    }

    fun deleteFavorite(food_id: Int) {//Favori yemeği siler.
        CoroutineScope(Dispatchers.Main).launch {
            foodsRepository.deleteFavorite(food_id)
            favoriteFoodsLoad()
        }
    }
}