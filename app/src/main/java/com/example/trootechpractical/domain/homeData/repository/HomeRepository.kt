package com.example.trootechpractical.domain.homeData.repository

import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.homeData.model.UserModel
import kotlinx.coroutines.flow.Flow

/**
 * Interface of HomeRepository to expose to other module
 */
interface HomeRepository {

    /**
     * Method to getUserData from Repository
     * @return Flow of Outputs with UserList
     */
    suspend fun getUserData(): Flow<Output<ArrayList<UserModel>>>
}