package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.network.api.APIService
import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.ResponseUser
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class SplashViewModel @ViewModelInject internal constructor(
    private val cacheDao: CacheDao,
    private val apiService: APIService
) : ObservableViewModel() {
    //We can store User info to cache dao..

    fun firstLaunch() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        val userData = cacheDao.getCacheData(AppConstants.USER_INFO)
        if (userData == null) {
            emit(true)
        } else {
            //First get token from old then ask for refresh. again save new token
            val listType = object : TypeToken<ResponseUser>() {}.type
            var userInfo = Gson().fromJson<ResponseUser>(userData.value, listType)

            Timber.i("Already signed from database $userInfo")
            AppHeaders.updateUserData(userInfo)
            //We need to refresh token..
            apiService.refreshToken(mapOf("auth" to AppHeaders.token)).body()?.let {
                userInfo = it
                Timber.i("Already signed from network $userInfo")
            }
            val userData = Gson().toJson(userInfo)
            cacheDao.insert(Cache(AppConstants.USER_INFO, userData))
            AppHeaders.updateUserData(userInfo)
            Timber.i("Already signed from network final $userInfo")
            //Now we need to get information about shop
            emit(false)
        }
    }
}