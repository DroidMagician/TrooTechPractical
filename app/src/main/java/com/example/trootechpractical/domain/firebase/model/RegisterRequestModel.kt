package com.example.trootechpractical.domain.firebase.model

import com.google.gson.annotations.SerializedName

data class RegisterRequestModel(
    @SerializedName("password") var password: String  = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
)


