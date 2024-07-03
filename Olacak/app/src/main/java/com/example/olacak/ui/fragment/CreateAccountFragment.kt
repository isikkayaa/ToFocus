package com.example.olacak.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.olacak.R
import com.example.olacak.data.entity.User
import com.example.olacak.databinding.FragmentCreateAccountBinding
import com.example.olacak.room.UserDao
import com.example.olacak.util.PasswordUtils
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateAccountFragment : Fragment() {

    @Inject
    lateinit var userDao: UserDao

    private lateinit var binding: FragmentCreateAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonSignUp.setOnClickListener { a ->

            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            val confirmPass = binding.passwordconfirmEditText.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val hashedPassword = PasswordUtils.hashPassword(pass)
                            lifecycleScope.launch {
                                val newUser = User(email = email, passwordHash = hashedPassword)
                                val userId = userDao.insert(newUser).toInt()
                                registerUser(userId)
                                Navigation.findNavController(a).navigate(R.id.action_createAccountFragment_to_loginFragment)
                            }
                        } else {
                            Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Empty Fields Are Not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun registerUser(userId: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("user_id", userId).apply()
    }


}


