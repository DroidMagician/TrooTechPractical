package com.example.trootechpractical.presentation.home.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trootechpractical.domain.firebase.model.FirebaseUserModel
import com.google.gson.Gson
import com.example.trootechpractical.domain.homeData.usecase.HomeUseCase
import com.example.trootechpractical.presentation.base.BaseViewModel
import com.example.trootechpractical.utils.SharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val homeUseCase: HomeUseCase, private val application: Application) : BaseViewModel() {

    private val _loginUser = MutableLiveData<FirebaseUserModel>()
    val loginUser: LiveData<FirebaseUserModel> = _loginUser


    init {
        var userobj = SharedPrefs(application).getLoginDetail()
        _loginUser.value = userobj!!
        Log.e("MyUser","Object is ${Gson().toJson(userobj)}")
    }

}