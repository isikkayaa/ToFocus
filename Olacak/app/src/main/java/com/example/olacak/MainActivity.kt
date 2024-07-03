package com.example.olacak

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.olacak.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreferences = getSharedPreferences("PomodoroSettings", Context.MODE_PRIVATE)
        val theme = sharedPreferences.getString("theme", "Light")

        if (theme == "Dark") {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.Theme_Olacak)
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigation()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                when (navController.currentDestination?.id) {
                    R.id.settingsFragment, R.id.profileFragment -> {
                        navController.navigate(R.id.anasayfaFragment)
                    }
                    else -> {
                        if (!navController.popBackStack()) {
                            finish()
                        }
                    }
                }
            }
        })
    }

    private fun setBottomNavigation() {
        bottomNavigationView = binding.bottomNavigationView
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.visibility = if (destination.id == R.id.girisFragment ||
                destination.id == R.id.loginFragment || destination.id == R.id.createAccountFragment) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }











    }







}