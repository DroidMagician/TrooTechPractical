package com.example.trootechpractical.presentation.firebaseLogin

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.trootechpractical.R
import com.example.trootechpractical.data.firebase.AuthRepository
import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.firebase.model.LoginRequestModel
import com.example.trootechpractical.presentation.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseLoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val application: Application
) :
//constructor injection
    BaseViewModel() {
    var loginRequestModel = LoginRequestModel()
    private val _loginResponse = MutableLiveData<Output<FirebaseUser>>()
    val loginResponse: LiveData<Output<FirebaseUser>> = _loginResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun login() = viewModelScope.launch {
        if (validateInput()) {
            _loginResponse.value = Output(Output.Status.LOADING)
            val result =
                repository.login(loginRequestModel.email ?: "", loginRequestModel.password ?: "")
            _loginResponse.value = result
        }

}

private fun validateInput(): Boolean {
    if (loginRequestModel.email.isNullOrBlank()) {
        _errorMessage.value = application.getString(R.string.please_enter_email)
        return false
    } else if (loginRequestModel.password.isNullOrBlank()) {
        _errorMessage.value = application.getString(R.string.please_enter_password)
        return false
    } else {
        return true
    }
}
}