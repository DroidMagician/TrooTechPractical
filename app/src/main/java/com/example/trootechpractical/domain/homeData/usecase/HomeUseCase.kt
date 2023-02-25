package com.example.trootechpractical.domain.homeData.usecase

import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.homeData.model.UserModel
import kotlinx.coroutines.flow.Flow

/**
 * Interface of Login UseCase to expose to ui module
 */
interface HomeUseCase {
    /**
     * UseCase Method to getUserList from Data Layer
     */
    suspend fun execute(): Flow<Output<ArrayList<UserModel>>>
}