package com.osmancandincer.myfoodorderapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.osmancandincer.myfoodorderapp.data.entity.FavoriteFoods

@Dao
interface FavoriteFoodsDao {

    @Query("SELECT * FROM favorite")
    suspend fun favoriteFoodsLoad(): List<FavoriteFoods>

    @Insert
    suspend fun addFavorite(favoriteFoods: FavoriteFoods)

    @Query("DELETE FROM favorite WHERE food_id = :food_id")
    suspend fun deleteFavorite(food_id: Int)

    @Query("SELECT COUNT(*) FROM favorite WHERE food_id = :food_id")
    suspend fun isFavorite(food_id: Int): Int

}