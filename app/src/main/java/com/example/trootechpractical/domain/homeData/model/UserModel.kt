package com.example.trootechpractical.domain.homeData.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class UserModel(
    @SerializedName("id") var id: String? = null,
    @SerializedName("author") var author: String?= null,
    @SerializedName("width") var width: String?= null,
    @SerializedName("height") var height:String?= null,
    @SerializedName("url") var url:String?= null,
    @SerializedName("download_url") var download_url:String?= null,
    @SerializedName("date") var date:String?= null,
     var isSelected:Boolean = false,
) : Parcelable