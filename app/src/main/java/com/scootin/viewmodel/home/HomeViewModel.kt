package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

import androidx.lifecycle.viewModelScope
import com.scootin.database.dao.CacheDao
import com.scootin.extension.MakeLiveData
import com.scootin.network.AppExecutors
import com.scootin.repository.UserRepository
import com.scootin.util.constants.AppConstants
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers


class HomeViewModel @ViewModelInject internal constructor(
    private val userRepo: UserRepository,
    private val cacheDao: CacheDao,
    private val executors: AppExecutors
) : ObservableViewModel()  {


    fun getRiderInfo(riderId: String) = userRepo.getRiderInfo(riderId, viewModelScope.coroutineContext + Dispatchers.IO)
    private val _doLogout = MutableLiveData<Boolean>()

    fun doLogout() {
        _doLogout.postValue(true)
    }
    val logoutComplete = Transformations.switchMap(_doLogout) { request->
        if (request) {
            executors.diskIO().execute {
                cacheDao.delete(AppConstants.USER_INFO)
            }
        }
        MakeLiveData.create(true)
    }


}