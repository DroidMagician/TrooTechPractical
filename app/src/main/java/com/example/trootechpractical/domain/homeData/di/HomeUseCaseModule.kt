package com.example.trootechpractical.domain.homeData.di

import com.example.trootechpractical.domain.homeData.usecase.HomeUseCase
import com.example.trootechpractical.domain.homeData.usecase.HomeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeUseCaseModule {
    @Binds
    @Singleton
    internal abstract fun bindLoginUseCaseUseCase(useCaseImpl: HomeUseCaseImpl): HomeUseCase
}