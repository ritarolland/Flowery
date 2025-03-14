package com.example.prac1.di

import android.content.Context
import android.content.SharedPreferences
import com.example.prac1.data.repository.AuthRepositoryImpl
import com.example.prac1.data.repository.CartRepositoryImpl
import com.example.prac1.data.repository.FlowersRepositoryImpl
import com.example.prac1.data.repository.ProfileRepositoryImpl
import com.example.prac1.data.repository.TokenRepositoryImpl
import com.example.prac1.data.repository.UserUidRepositoryImpl
import com.example.prac1.domain.repository.AuthRepository
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.FlowersRepository
import com.example.prac1.domain.repository.ProfileRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.domain.repository.UserUidRepository
import com.example.prac1.data.api.FlowerApi
import com.example.prac1.data.db.FlowerDao
import com.example.prac1.data.repository.FavouritesRepositoryImpl
import com.example.prac1.domain.repository.FavouritesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFlowersRepository(api: FlowerApi, dao: FlowerDao): FlowersRepository {
        return FlowersRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideFavouritesRepository(api: FlowerApi, uidRepository: UserUidRepository, tokenRepository: TokenRepository): FavouritesRepository {
        return FavouritesRepositoryImpl(api, uidRepository, tokenRepository)
    }

    @Provides
    @Singleton
    fun provideUserUidRepository(
        sharedPreferences: SharedPreferences,
        tokenRepository: TokenRepository,
        api: FlowerApi
    ): UserUidRepository {
        return UserUidRepositoryImpl(
            sharedPreferences,
            tokenRepository,
            api
        )
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        api: FlowerApi,
        tokenRepository: TokenRepository,
        userUidRepository: UserUidRepository
    ): CartRepository {
        return CartRepositoryImpl(api, tokenRepository, userUidRepository)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(sharedPreferences: SharedPreferences, api: FlowerApi): TokenRepository {
        return TokenRepositoryImpl(sharedPreferences, api)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: FlowerApi, tokenRepository: TokenRepository): AuthRepository {
        return AuthRepositoryImpl(api, tokenRepository)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(api: FlowerApi, uidRepository: UserUidRepository): ProfileRepository {
        return ProfileRepositoryImpl(api, uidRepository)
    }
}