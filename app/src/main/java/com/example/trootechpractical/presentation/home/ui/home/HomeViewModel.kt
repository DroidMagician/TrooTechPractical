package com.example.trootechpractical.presentation.home.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.firebase.model.LoginRequestModel
import com.example.trootechpractical.domain.homeData.model.UserModel
import com.example.trootechpractical.domain.homeData.usecase.HomeUseCase
import com.example.trootechpractical.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUseCase: HomeUseCase, private val application: Application) : BaseViewModel() {


    private val _userListResponse = MutableLiveData<Output<ArrayList<UserModel>>>()
    val userListResponse: LiveData<Output<ArrayList<UserModel>>> = _userListResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        getUserList()
    }
    fun getUserList() {
        viewModelScope.launch {
            homeUseCase.execute().collect {
                _userListResponse.value = it
                Log.e("FInal Output","Is ${_userListResponse.value!!.data?.size}")
            }
        }

    }
}