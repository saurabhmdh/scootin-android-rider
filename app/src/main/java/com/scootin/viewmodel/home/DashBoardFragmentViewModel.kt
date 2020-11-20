package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.network.RequestFCM
import com.scootin.network.api.APIService
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestActive
import com.scootin.network.request.RiderLocationDTO
import com.scootin.repository.OrderRepository
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class DashBoardFragmentViewModel @ViewModelInject
internal constructor(
    private val userRepository: UserRepository,
    private val cacheDao: CacheDao,
    private val orderRepository: OrderRepository,
    private val apiService: APIService

) : ObservableViewModel(), CoroutineScope {

    fun updateFCMID(token: String?) {
        //get current FCM ID and its not same as current, We will send to server..
        launch {
            token?.let {
                val cache = cacheDao.getCacheData(AppConstants.FCM_ID)
                if ((cache == null || cache.value != it) && AppHeaders.userID.isEmpty().not()) {
                    Timber.i("Data which need to update to server user ${AppHeaders.userID} value $it")
                    userRepository.updateFCMId(AppHeaders.userID, RequestFCM(it))
                    cacheDao.insert(Cache(AppConstants.FCM_ID, it))
                }
            }
        }
    }

    private val ONLINE = "online"

    fun onlineStatus() = cacheDao.getData(ONLINE)

    fun updateStatus(riderId: String, status: Boolean) {
        launch{
            val cache = Cache(ONLINE, status.toString())
            cacheDao.insert(cache)
            userRepository.updateStatus(riderId, RequestActive(status))
        }
    }

    fun countDeliverOrders(riderId: String) = orderRepository.countDeliverOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)

    fun countReceivedOrders(riderId: String) = orderRepository.countReceivedOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)


    val _locationData = MutableLiveData<RiderLocationDTO>()


    //TODO: After confirmation we can enable this
//    val searchResult = _locationData.asFlow().debounce(AppConstants.LOCATION_DEBOUNCE_TIME).mapLatest {
//        Timber.i("Running code ${it}")
////        apiService.updateLocation(AppHeaders.userID, it)
//    }.catch {
//        Timber.i("Some error code")
//    }.asLiveData()

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO

}