package com.osmancandincer.myfoodorderapp.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.osmancandincer.myfoodorderapp.data.datasource.FoodsDataSource
import com.osmancandincer.myfoodorderapp.data.repo.FoodsRepository
import com.osmancandincer.myfoodorderapp.retrofit.ApiUtils
import com.osmancandincer.myfoodorderapp.retrofit.FoodsDao
import com.osmancandincer.myfoodorderapp.room.FavFoodsDataBase
import com.osmancandincer.myfoodorderapp.room.FavoriteFoodsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFoodsDataSource(
        foodsDao: FoodsDao,
        favoriteFoodsDao: FavoriteFoodsDao
    ): FoodsDataSource {
        return FoodsDataSource(foodsDao, favoriteFoodsDao)
    }

    @Provides
    @Singleton
    fun provideFoodsRepository(foodsDataSource: FoodsDataSource): FoodsRepository {
        return FoodsRepository(foodsDataSource)
    }

    @Provides
    @Singleton
    fun provideFoodsDao(): FoodsDao {
        return ApiUtils.getFoodsDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteFoodsDao(@ApplicationContext context: Context): FavoriteFoodsDao {
        val vt = Room.databaseBuilder(context, FavFoodsDataBase::class.java, "favfood.sqlite")
            .createFromAsset("favfood.sqlite").build()
        return vt.getFavoriteFoodsDao()
    }
}