package com.turnsapp.network.repository

import com.turnsapp.network.ApiService
import com.turnsapp.network.ResponseHandler
import com.turnsapp.network.models.moviedetails.MovieDetailsResponse
import com.turnsapp.network.models.movielist.MovieResponse
import com.turnsapp.types.MovieListTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


interface MovieRepository {

    suspend fun getMovies(type:MovieListTypes, page:Int): Flow<ResponseHandler<MovieResponse>>

    suspend fun getMovieDetails(movieId:Int): Flow<ResponseHandler<MovieDetailsResponse>>

}

class MovieRepositoryImpl(private val retrofitService:ApiService) : MovieRepository {

    override suspend fun getMovies(type:MovieListTypes, page: Int): Flow<ResponseHandler<MovieResponse>> = flow {

        try {
            val getResponse = when(type) {
                MovieListTypes.PlayingNow -> retrofitService.getNowPlayingMovies(page = page)
                MovieListTypes.Popular -> retrofitService.getPopularMovies(page = page)
                MovieListTypes.TopRated -> retrofitService.getTopRatedMovies(page = page)
                MovieListTypes.Upcoming -> retrofitService.getUpcomingMovies(page = page)
            }

            getResponse.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let { movieResponse ->
                        emit(ResponseHandler.Success(movieResponse))
                    }
                }else{
                    emit(
                        ResponseHandler.Error(
                            message = "Error in get movies",
                            data = null
                        )
                    )
                }
            }
        }catch (e:Exception) {
            e.printStackTrace()
            emit(
                ResponseHandler.Error(
                    message = "Error in get products",
                    data = null
                )
            )
        }
    }

    override suspend fun getMovieDetails(movieId:Int): Flow<ResponseHandler<MovieDetailsResponse>> = flow {

        try {
            val getResponse = retrofitService.getMovieDetails(movieId = movieId)

            getResponse.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let { movieResponse ->
                        emit(ResponseHandler.Success(movieResponse))
                    }
                }else{
                    emit(
                        ResponseHandler.Error(
                            message = "Error in get movies",
                            data = null
                        )
                    )
                }
            }
        }catch (e:Exception) {
            e.printStackTrace()
            emit(
                ResponseHandler.Error(
                    message = "Error in get products",
                    data = null
                )
            )
        }
    }
}