package com.scootin.viewmodel.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.scootin.repository.OrderRepository
import com.scootin.repository.UserRepository
import com.scootin.viewmodel.base.ObservableViewModel
import kotlinx.coroutines.Dispatchers

class UserViewModel @ViewModelInject
internal constructor(
    private val userRepository: UserRepository
) : ObservableViewModel() {

    fun getRiderInfo(riderId:String) = userRepository.getRiderInfo(riderId,viewModelScope.coroutineContext + Dispatchers.IO)
}