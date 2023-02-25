package com.example.trootechpractical.presentation.home.ui

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.trootechpractical.R
import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.firebase.model.LoginRequestModel
import com.example.trootechpractical.domain.homeData.model.UserModel
import com.example.trootechpractical.domain.homeData.usecase.HomeUseCase
import com.example.trootechpractical.presentation.base.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeDataViewModel @Inject constructor(private val application: Application) :
    //constructor injection
    BaseViewModel() {

}