package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.network.RequestFCM
import com.scootin.network.api.CacheNetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.manager.AppHeaders
import com.scootin.repository.OrderRepository
import com.scootin.repository.TempleRepo
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
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

    fun updateStatus(status: Boolean) {
        launch{
            //TODO: we need send this information to server
            val cache = Cache(ONLINE, status.toString())
            cacheDao.insert(cache)
        }
    }

    fun countDeliverOrders(riderId: String) = orderRepository.countDeliverOrders(riderId, viewModelScope.coroutineContext + Dispatchers.IO)

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext + Dispatchers.IO

}