package com.example.trootechpractical.domain.homeData.usecase
import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.homeData.model.UserModel
import com.example.trootechpractical.domain.homeData.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of LoginUseCase
 * @param homeRepository the HomeRepository object
 */
internal class HomeUseCaseImpl @Inject constructor(private val homeRepository: HomeRepository) :
    HomeUseCase {

    override suspend fun execute(): Flow<Output<ArrayList<UserModel>>> {
        return homeRepository.getUserData()
    }
}
