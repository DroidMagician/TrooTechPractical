package com.example.trootechpractical.data.homeData.repository
import android.util.Log
import com.google.gson.Gson
import com.example.trootechpractical.data.homeData.remote.HomeRemoteDataSource
import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.homeData.model.UserModel
import com.example.trootechpractical.domain.homeData.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Implementation of HomeRepository
 * @param homeRemoteDataSource the object of remote data source
 */
internal class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDataSource:  HomeRemoteDataSource
) : HomeRepository {
    override suspend fun getUserData(): Flow<Output<ArrayList<UserModel>>> {
        return flow {
            emit(Output.loading())
            val result = homeRemoteDataSource.getUserList();
            Log.e("Inside Repository","Data == ${Gson().toJson(result)}")
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}