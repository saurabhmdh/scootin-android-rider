package com.scootin.view.fragment.home.dashboard


import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.birjuvachhani.locus.Locus
import com.google.firebase.iid.FirebaseInstanceId
import com.scootin.R
import com.scootin.databinding.FragmentDashboardBinding
import com.scootin.location.LocationService
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RiderLocationDTO
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.home.DashBoardFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private var binding by autoCleared<FragmentDashboardBinding>()
    private val viewModel: DashBoardFragmentViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        binding.pendingOrdersTab.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionHomeFragmentToPendingOrdersFragment())
        }
        binding.completedOrdersTab.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionHomeFragmentToCompletedOrdersFragment())
        }
        binding.acceptedOrdersTab.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionHomeFragmentToAcceptOrdersFragment())
        }

        binding.onlineBtn.setOnClickListener {
            Timber.i("Status = ${binding.onlineBtn.isSelected}")
            viewModel.updateStatus(AppHeaders.userID, binding.onlineBtn.isSelected)
        }
//        configureLocus()
        updateFirebaseInformation()
        updateListeners()
    }

//    private fun configureLocus() {
//        Locus.configure {
//            enableBackgroundUpdates = true
//            forceBackgroundUpdates = true
//            shouldResolveRequest = true
//        }
//    }

    private fun updateListeners() {
        viewModel.onlineStatus().observe(viewLifecycleOwner) { cache->
            if (cache == null) {
                binding.onlineBtn.isSelected = false
            } else {
                Timber.i("Cache Status = ${cache.value.toBoolean()}")
                binding.onlineBtn.isSelected = cache.value.toBoolean().not()
//                if (cache.value.toBoolean()){
//                    startLocationService()
//                    startUpdates()
//                }else {
//                    stopUpdates()
//                }
            }
        }

        viewModel.countDeliverOrders(AppHeaders.userID).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.orderDelivered.text = it.data.toString()
                }
            }
        }

        viewModel.countReceivedOrders(AppHeaders.userID).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.ordersReceived.text = it.data.toString()
                }
            }
        }
//        viewModel.searchResult.observe(viewLifecycleOwner) {
//            //Do nothing..
//        }
    }


    private fun updateFirebaseInformation() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if(!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = it.result?.token
            Timber.i("Saurabh : Device token $token ")
            viewModel.updateFCMID(token)
        }
    }


    private fun onLocationUpdate(location: Location) {
        Timber.i("Latitude: ${location.latitude}\tLongitude: ${location.longitude}")
        viewModel._locationData.postValue(RiderLocationDTO(location.latitude, location.longitude))
    }

    private fun onError(error: Throwable?) {
        Timber.e("Error: ${error?.message}")
    }


    private fun startUpdates() {
        Locus.startLocationUpdates(this) { result ->
            result.location?.let(::onLocationUpdate)
            result.error?.let(::onError)
        }
    }

    private fun stopUpdates() {
        Locus.stopLocationUpdates()
        context?.stopService(Intent(context, LocationService::class.java))
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        manager?.cancel(LocationService.NOTIFICATION_ID)
    }

    fun startLocationService() {
        requireContext().startService(Intent(requireContext(), LocationService::class.java))
    }


}