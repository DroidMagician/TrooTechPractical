package com.example.trootechpractical.presentation.home.ui.logout

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trootechpractical.presentation.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(private val application: Application) : BaseViewModel() {

    fun doLogout()
    {
        FirebaseAuth.getInstance().signOut()
    }
}