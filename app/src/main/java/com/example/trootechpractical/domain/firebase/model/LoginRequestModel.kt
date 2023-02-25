package com.example.trootechpractical.domain.firebase.model

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
   @SerializedName("password") var password: String? = null,
   @SerializedName("email") var email: String? = null,
)


