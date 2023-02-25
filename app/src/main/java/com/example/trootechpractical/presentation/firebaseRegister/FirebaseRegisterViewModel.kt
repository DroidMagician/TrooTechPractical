package com.example.trootechpractical.presentation.firebaseRegister

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.trootechpractical.R
import com.example.trootechpractical.data.firebase.AuthRepository
import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.firebase.model.RegisterRequestModel
import com.example.trootechpractical.presentation.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseRegisterViewModel @Inject constructor(private val repository: AuthRepository, private val application: Application) :
    BaseViewModel() {
    var registerRequestModel = RegisterRequestModel()

    private val _registerResponse = MutableLiveData<Output<FirebaseUser>>()
    val registerResponse: LiveData<Output<FirebaseUser>> = _registerResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


   //Do Register User in Firebase
    fun signup(fileUri:String?) = viewModelScope.launch {
        if(validateInput())
        {
            _registerResponse.value = Output(Output.Status.LOADING)
            val result = repository.signup(registerRequestModel.name, registerRequestModel.email, registerRequestModel.password,fileUri)
            _registerResponse.value = result
        }
    }
    //Validate User Input
    private fun validateInput(): Boolean {
        if(registerRequestModel.name.isNullOrBlank())
        {
            _errorMessage.value = application.getString(R.string.please_enter_name)
            return false
        }
        else if(registerRequestModel.email.isNullOrBlank())
        {
            _errorMessage.value = application.getString(R.string.please_enter_email)
            return false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(registerRequestModel.email.trim()).matches()) {
            _errorMessage.value = application.getString(R.string.enter_valid_email)
            return false
        }
        else if(registerRequestModel.password.isNullOrBlank())
        {
            _errorMessage.value = application.getString(R.string.please_enter_password)
            return false
        }
        else return true
    }
}