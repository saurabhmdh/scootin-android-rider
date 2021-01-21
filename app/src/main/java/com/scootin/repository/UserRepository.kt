package com.scootin.repository

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.network.RequestFCM
import com.scootin.network.api.*
import com.scootin.network.request.RequestActive
import com.scootin.network.response.DirectOrderResponse
import com.scootin.network.response.ResponseUser
import com.scootin.network.response.RiderInfo
import com.scootin.network.response.UserInfo
import com.scootin.util.constants.AppConstants
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class UserRepository @Inject constructor(
    private val services: APIService,
    private val cacheDao: CacheDao
) {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.HOURS)

    private val KEY = "special_feature"

    fun sendOTP(
        options: Map<String, String>,
        context: CoroutineContext
    ): LiveData<Resource<ResponseBody>> = object : NetworkBoundResource<ResponseBody>(context) {
        override suspend fun createCall(): Response<ResponseBody> = services.requestOTP(options)
    }.asLiveData()


    fun doLogin(
        options: Map<String, String>,
        context: CoroutineContext
    ): LiveData<Resource<ResponseUser>> = object : NetworkBoundResource<ResponseUser>(context) {
        override suspend fun createCall(): Response<ResponseUser> = services.doLogin(options)
    }.asLiveData()

    suspend fun updateFCMId(id: String, request: RequestFCM) = services.updateFCMID(id, request)


    fun getRiderInfo(riderId: String, coroutineContext: CoroutineContext): LiveData<out Resource<RiderInfo>> = object : CacheNetworkBoundResource<RiderInfo>(coroutineContext) {
        override suspend fun saveCallResult(item: RiderInfo) {
            cacheDao.insert(Cache(AppConstants.RIDER_INFO,  Gson().toJson(item)))
        }

        override fun shouldFetch(data: RiderInfo?): Boolean {
            val timeout = repoListRateLimit.shouldFetch(KEY)
            return data == null || timeout
        }

        override suspend fun loadFromDb(): RiderInfo? {
            val data = cacheDao.getCacheData(AppConstants.RIDER_INFO)

            return if (data == null) null else {
                val decode = decode(data.value)
                decode
            }
        }

        override suspend fun createCall(): Response<RiderInfo> = services.getRiderInfo(riderId)
    }.asLiveData()

    fun decode(data: String): RiderInfo? {
        val listType = object : TypeToken<RiderInfo>() {}.type
        return  Gson().fromJson<RiderInfo>(data, listType)
    }

    suspend fun updateStatus(riderId: String, requestActive: RequestActive) = services.updateStatus(riderId, requestActive)
}