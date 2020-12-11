package com.scootin.view.activity

//import com.birjuvachhani.locus.Locus
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.scootin.R
import com.scootin.bindings.setCircleImage
import com.scootin.databinding.ActivityMainBinding
import com.scootin.interfaces.IFullScreenListener
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    IFullScreenListener {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private val topLevelDestinations = setOf(R.id.nav_dashboard)

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val APP_UPDATE_TYPE_SUPPORTED = AppUpdateType.IMMEDIATE
    private val REQUEST_UPDATE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForUpdates()

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


    private fun checkForUpdates() {
        val appUpdateManager = AppUpdateManagerFactory.create(baseContext)
        val appUpdateInfo = appUpdateManager.appUpdateInfo
        appUpdateInfo.addOnSuccessListener {
            //2
            handleUpdate(appUpdateManager, appUpdateInfo)
        }
    }

    private fun handleUpdate(manager: AppUpdateManager, info: Task<AppUpdateInfo>) {
        if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.IMMEDIATE) {
            handleImmediateUpdate(manager, info)
        } else if (APP_UPDATE_TYPE_SUPPORTED == AppUpdateType.FLEXIBLE) {
//            handleFlexibleUpdate(manager, info)
        }
    }

    private fun handleImmediateUpdate(manager: AppUpdateManager, info: Task<AppUpdateInfo>) {
        Log.e(TAG, "handleImmediateUpdate -> ${info.result.updateAvailability()}, ${UpdateAvailability.UPDATE_AVAILABLE}")
        Timber.i(TAG,"handleImmediateUpdate -> ${info.result.updateAvailability()}, ${UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS}")
        Timber.i("handleImmediateUpdate -> ${info.result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)}")

        if ((info.result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    || info.result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)
            && info.result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
        ) {
            Timber.i(TAG,"handleImmediateUpdate -> everything done well now waiting to update..")
            manager.startUpdateFlowForResult(info.result, AppUpdateType.IMMEDIATE, this, REQUEST_UPDATE)
        }
    }

    // Checks that the update is not stalled during 'onResume()'.
// However, you should execute this check at all entry points into the app.
    override fun onResume() {
        super.onResume()
        val appUpdateManager = AppUpdateManagerFactory.create(baseContext)
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    Timber.i(TAG,"onResume -> update is in progress..")
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        REQUEST_UPDATE
                    );
                }
            }
    }

}
