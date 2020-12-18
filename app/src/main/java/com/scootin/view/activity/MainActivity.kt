package com.scootin.view.activity

//import com.birjuvachhani.locus.Locus
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.observe
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.scootin.R
import com.scootin.bindings.setCircleImage
import com.scootin.databinding.ActivityMainBinding
import com.scootin.interfaces.IFullScreenListener
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    IFullScreenListener {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private val topLevelDestinations = setOf(R.id.nav_dashboard)

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }


        setUpToolbar()

        navController = findNavController(R.id.home_navigation_fragment)

        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setOpenableLayout(binding.drawerLayout)
            .build()

        binding.navView.setupWithNavController(navController)
        binding.navView.setItemIconTintList(null)
        binding.navView.setNavigationItemSelectedListener(this)

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupListener()

        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(listener, IntentFilter(AppConstants.INTENT_ACTION_USER_DISABLED))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(listener)
        super.onDestroy()
    }

    private fun setupListener() {
        viewModel.getRiderInfo(AppHeaders.userID).observe(this) {
            when(it.status) {
                Status.SUCCESS -> {
                    it.data?.let {riderInfo->
                        // binding.navView.removeHeaderView(it)
                        val imageV = binding.navView.findViewById<ImageView>(R.id.imgpendingorders)
                        imageV.setCircleImage(riderInfo.profilePictureReference.url)

                        val nameView = binding.navView.findViewById<TextView>(R.id.name)
                        nameView.setText(riderInfo.first_name)

                        val email = binding.navView.findViewById<TextView>(R.id.email)
                        email.setText(riderInfo.email)
                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }

        }
        handleLogOut()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    @SuppressLint("ResourceType")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu -> {
                navController.navigate(R.id.settings_menu)
                binding.drawerLayout.closeDrawers()
            }
           R.id.logout_menu->{
               viewModel.doLogout()
               binding.drawerLayout.closeDrawers()
           }
        }

        return true
    }

    override fun onBackPressed() {
        val drawer = binding.drawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(navController, appBarConfiguration)

    private fun handleLogOut() {
        viewModel.logoutComplete.observe(this) {
            gotoStart()
        }
    }
    private fun gotoStart() {
        startActivity(Intent(this, SplashActivity::class.java))
        this.overridePendingTransition(R.anim.enter, R.anim.exit)
        this.finish()
    }

    override fun showHideActionBar(visible: Boolean) {
        if (visible) {
            binding.toolbar.visibility = View.VISIBLE
        } else {
            binding.toolbar.visibility = View.GONE
        }
    }

    private val listener = MyBroadcastReceiver()

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent){
            when (intent.action) {
                AppConstants.INTENT_ACTION_USER_DISABLED -> {
                    Toast.makeText(context, "Account has been disabled by admin", Toast.LENGTH_LONG).show()
                    finish()
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
            }

        }
    }
}
