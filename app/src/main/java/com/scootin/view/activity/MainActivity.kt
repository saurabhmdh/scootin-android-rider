package com.scootin.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.scootin.R
import com.scootin.databinding.ActivityMainBinding
import com.scootin.view.fragment.home.HomeFragment
import com.scootin.view.fragment.home.dashboard.DashboardFragment
import com.scootin.view.fragment.home.orders.OrdersFragment
import com.scootin.view.fragment.home.settings.SettingsFragment

import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var previousMenuItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setUpToolbar()
        binding.drawerLayout.closeDrawers()
        if (savedInstanceState == null) {
            setupNavigationBar()
        }


    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupNavigationBar()
    }

    private fun setupNavigationBar(){
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.navView.setNavigationItemSelectedListener {
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.frameLayout.id, DashboardFragment())
                        .commit()
                    supportActionBar?.title = "Dashboard"
                    binding.drawerLayout.closeDrawers()
                }
                R.id.orders_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.frameLayout.id,OrdersFragment())
                        .commit()
                    supportActionBar?.title = "Orders"
                    binding.drawerLayout.closeDrawers()
                }
                R.id.settings_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.frameLayout.id,SettingsFragment())
                        .commit()
                    supportActionBar?.title = "Settings"
                    binding.drawerLayout.closeDrawers()
                }


            }

            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle("Dashboard")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}
