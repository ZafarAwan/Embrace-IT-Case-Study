package com.embrace.casestudy.di

import android.content.Context
import androidx.room.Room
import com.embrace.casestudy.network.webService.ApiService
import com.embrace.casestudy.utils.NetworkComponents
import com.embrace.casestudy.network.database.RoomDataBase
import com.embrace.casestudy.network.database.RoomQuizDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor)

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient.Builder): ApiService =
        Retrofit.Builder()
            .baseUrl(NetworkComponents.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
            .create(ApiService::class.java)


    @Singleton
    @Provides
    fun getRoomDb(@ApplicationContext context: Context) : RoomQuizDao =
        Room.databaseBuilder(context, RoomDataBase::class.java,"RoomDb").build().getQuestionDao()
}