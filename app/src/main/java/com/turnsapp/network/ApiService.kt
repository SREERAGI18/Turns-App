package com.turnsapp.network

import com.turnsapp.BuildConfig
import com.turnsapp.network.models.moviedetails.MovieDetailsResponse
import com.turnsapp.network.models.movielist.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("page") page:Int = 1
    ):Response<MovieResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("page") page:Int = 1
    ):Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("page") page:Int = 1
    ):Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("page") page:Int = 1
    ):Response<MovieResponse>

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId:Int,
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("page") page:Int = 1,
    ):Response<MovieDetailsResponse>

}

