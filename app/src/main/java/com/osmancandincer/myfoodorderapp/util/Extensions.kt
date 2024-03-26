package com.osmancandincer.myfoodorderapp.util

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

fun Navigation.change(it : View, id:Int) {
    findNavController(it).navigate(id)
}

fun Navigation.change(it : View, id: NavDirections) {
    findNavController(it).navigate(id)
}