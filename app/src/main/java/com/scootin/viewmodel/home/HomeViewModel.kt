package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject

import androidx.lifecycle.viewModelScope
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers


class HomeViewModel @ViewModelInject internal constructor(
    private val userRepo: UserRepository
) : ObservableViewModel()  {


    fun getRiderInfo(riderId: String) = userRepo.getRiderInfo(riderId, viewModelScope.coroutineContext + Dispatchers.IO)



}