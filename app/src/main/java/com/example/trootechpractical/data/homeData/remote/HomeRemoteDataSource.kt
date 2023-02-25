package com.example.trootechpractical.data.homeData.remote

import android.app.Application
import android.util.Log
import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.homeData.model.UserModel
import com.google.gson.Gson
import org.json.JSONArray
import java.io.IOException
import javax.inject.Inject


/**
 * RemoteDataSource of Chara010cters API service
 * @param apiService the object of api service
 */
class HomeRemoteDataSource @Inject constructor( private val application: Application
) {

    /**
     * Method to fetch the characters from CharsRemoteDataSource
     * @return Outputs with list of Characters
     */
    suspend fun getUserList(): Output<ArrayList<UserModel>> {
       return Output.success(bindJSONDataInFacilityList())
    }

    private fun bindJSONDataInFacilityList(): ArrayList<UserModel> {
        var facilityModelList = ArrayList<UserModel>()

        val facilityJsonArray = JSONArray( application.assets.open("userData.json").bufferedReader().use { reader ->
            reader.readText()
        }) // Extension Function call here
        //Log.e("Json String","Here $facilityJsonArray")
        for (i in 0 until facilityJsonArray.length()){
            val facilityModel = UserModel()
            val facilityJSONObject = facilityJsonArray.getJSONObject(i)
            facilityModel.id = facilityJSONObject.getString("id")
            facilityModel.author = facilityJSONObject.getString("author")
            if(facilityJSONObject.has("date"))
            {
                facilityModel.date = facilityJSONObject.getString("date")
            }
            facilityModel.height = facilityJSONObject.getString("height")
            facilityModel.url = facilityJSONObject.getString("url")
            facilityModel.download_url = facilityJSONObject.getString("download_url")
            facilityModel.width = facilityJSONObject.getString("width")

            facilityModelList.add(facilityModel)
        }
        return facilityModelList
    }
}