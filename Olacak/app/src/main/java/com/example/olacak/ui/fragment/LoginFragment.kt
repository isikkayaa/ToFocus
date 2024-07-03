package com.example.olacak.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.olacak.R
import com.example.olacak.data.entity.User
import com.example.olacak.databinding.FragmentLoginBinding
import com.example.olacak.room.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var userDao: UserDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.buttonLoginGoogle.setOnClickListener {
            signInGoogle()
        }

        binding.buttonLoginLogin.setOnClickListener { a ->
            val email = binding.emailloginEditText.text.toString()
            val pass = binding.passwordloginEditText.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        loginUser(email)
                        Navigation.findNavController(a).navigate(R.id.action_loginFragment_to_anasayfaFragment)
                    } else {
                        Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Empty Fields Are Not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun signInGoogle() {
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account)
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Google Sign-In failed", e)
            Toast.makeText(requireContext(), "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val email = account.email
                if (email != null) {
                    loginUser(email)
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_anasayfaFragment)
                }
            } else {
                Log.e("FirebaseAuth", "signInWithCredential:failure", task.exception)
                Toast.makeText(requireContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String) {
        lifecycleScope.launch {
            val user = userDao.getUserByEmail(email)
            if (user != null) {
                val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putLong("user_id", user.userId).apply()
            } else {

                val newUser = User(email = email, passwordHash = "")
                val userId = userDao.insert(newUser).toLong()
                val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putLong("user_id", userId).apply()
            }
        }
    }
}
