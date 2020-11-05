package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.network.RequestFCM
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestActive
import com.scootin.repository.OrderRepository
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class DashBoardFragmentViewModel @ViewModelInject
internal constructor(
    private val userRepository: UserRepository,
    private val cacheDao: CacheDao,
    private val orderRepository: OrderRepository

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

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO

}