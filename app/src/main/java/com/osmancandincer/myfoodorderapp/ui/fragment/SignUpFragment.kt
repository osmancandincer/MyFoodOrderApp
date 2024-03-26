package com.osmancandincer.myfoodorderapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.osmancandincer.myfoodorderapp.R
import com.osmancandincer.myfoodorderapp.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents() {
        binding.textViewSignIn.setOnClickListener {
            navControl.navigate(R.id.signUpToLogIn)

        }
        binding.buttonSignUp.setOnClickListener {
            val email = binding.emailET.text.toString()
            val password = binding.passET.text.toString()
            val password2 = binding.passET2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) {
                if (password == password2) {

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            //Toast.makeText(context, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                            navControl.navigate(R.id.signUpToLogIn)
                        } else {
                            Toast.makeText(context, it.exception?.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Girdiğiniz Şifreler Eşleşmiyor, Kontrol Edin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "Boş Alanlara İzin Verilmiyor, Kontrol Edin",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}