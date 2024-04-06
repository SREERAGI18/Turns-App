package com.turnsapp.di

import com.turnsapp.BuildConfig
import com.turnsapp.network.ApiService
import com.turnsapp.network.repository.MovieRepository
import com.turnsapp.network.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    //fun provideProductsDao(turnsDatabase: TurnsDatabase) = turnsDatabase.productsDao()
//fun provideSyncAddProductsDao(turnsDatabase: TurnsDatabase) = turnsDatabase.syncAddProductsDao()

    /**
     * method which returns [Retrofit] used to build Retrofit instance
     *       @return [Retrofit]
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL) // API_URL defined in gradle.properties
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * method which returns [ApiService] used to make all the http requests
     *       @return [ApiService]
     */
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    /**
     * method which returns [OkHttpClient] used to build retrofit service
     *       @return [OkHttpClient]
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {

        val builder = OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .callTimeout(5, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

        builder.addInterceptor(Interceptor { chain ->

            val request: Request = chain.request().newBuilder().build()

            chain.proceed(request)
        })
        if (BuildConfig.DEBUG) {
            // Http logging for showing http requests and responses in log
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideMovieRepository(apiService: ApiService): MovieRepositoryImpl {
        return MovieRepositoryImpl(apiService)
    }

}