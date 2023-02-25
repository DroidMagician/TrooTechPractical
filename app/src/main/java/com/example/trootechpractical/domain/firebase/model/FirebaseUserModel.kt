package com.example.trootechpractical.domain.firebase.model

import com.google.gson.annotations.SerializedName

data class FirebaseUserModel(
   @SerializedName("email") var email: String? = null,
   @SerializedName("name") var name: String? = null,
   @SerializedName("profileImage") var profileImage: String? = null,
)


