package com.dicoding.soothemate

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.soothemate.databinding.ActivityMainBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.ui.onboarding.OnboardingActivity
import com.dicoding.soothemate.utils.Utils
import com.dicoding.soothemate.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var utils : Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        utils = Utils()

        utils.setTransparentStatusBar(this)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // Set listener for item selection to change icon dynamically
        navView.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId)
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateNavIcon(destination.id)
        }
        navView.itemIconTintList = getColorStateList(R.color.nav_item_color)
        navView.itemTextColor = getColorStateList(R.color.nav_item_text_color)

        navView.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId)
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateNavIcon(destination.id)
        }
        navView.itemIconTintList = getColorStateList(R.color.nav_item_color)
        navView.itemTextColor = getColorStateList(R.color.nav_item_text_color)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }

                R.id.navigation_add -> {
                    navController.navigate(R.id.navigation_add)
                    true
                }

                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }

                else -> false
            }
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }
        }

    }

    private fun updateNavIcon(selectedItemId: Int) {
        val menu = binding.navView.menu
        menu.findItem(R.id.navigation_home)?.setIcon(
            if (selectedItemId == R.id.navigation_home) R.drawable.ic_home_filled else R.drawable.ic_home_24dp
        )
        menu.findItem(R.id.navigation_add)?.setIcon(
            if (selectedItemId == R.id.navigation_add) R.drawable.ic_add_filled else R.drawable.ic_add_24dp
        )
        menu.findItem(R.id.navigation_profile)?.setIcon(
            if (selectedItemId == R.id.navigation_profile) R.drawable.ic_profile_filled else R.drawable.ic_profile_24dp
        )
    }

}