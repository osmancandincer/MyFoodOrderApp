package com.osmancandincer.myfoodorderapp.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.osmancandincer.myfoodorderapp.R

class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)

        val lottieAnimationView: LottieAnimationView = view.findViewById(R.id.animationView)
        lottieAnimationView.setAnimation(R.raw.intro_anim)
        lottieAnimationView.playAnimation()


        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if (auth.currentUser != null){
                navController.navigate(R.id.action_splashFragment_to_homeFragment)
            }else{
                navController.navigate(R.id.splashToLogIn)
            }
        }, 3000)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}