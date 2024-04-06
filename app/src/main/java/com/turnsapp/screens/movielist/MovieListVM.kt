package com.turnsapp.screens.movielist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turnsapp.R
import com.turnsapp.network.ResponseHandler
import com.turnsapp.network.models.movielist.MovieResponse
import com.turnsapp.network.repository.MovieRepositoryImpl
import com.turnsapp.types.MovieListTypes
import com.turnsapp.utils.isOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListVM @Inject constructor(
    private val movieRepository: MovieRepositoryImpl
):ViewModel() {

    val playingNowList = MutableStateFlow<ResponseHandler<MovieResponse>>(ResponseHandler.Idle("Idle State"))
    val popularList = MutableStateFlow<ResponseHandler<MovieResponse>>(ResponseHandler.Idle("Idle State"))
    val topRatedList = MutableStateFlow<ResponseHandler<MovieResponse>>(ResponseHandler.Idle("Idle State"))
    val upcomingList = MutableStateFlow<ResponseHandler<MovieResponse>>(ResponseHandler.Idle("Idle State"))

    private var playingNowPage = 1
    private var popularPage = 1
    private var topRatedPage = 1
    private var upcomingPage = 1

    private var playingNowMaxReached = false
    private var popularMaxReached = false
    private var topRatedMaxReached = false
    private var upcomingMaxReached = false

    fun getPlayingNowList(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            playingNowList.emit(ResponseHandler.Loading())
            if(context.isOnline()) {
                if(!playingNowMaxReached) {
                    movieRepository.getMovies(MovieListTypes.PlayingNow, playingNowPage).collect {
                        if(it.data?.totalPages == playingNowPage) {
                            playingNowMaxReached = true
                        }else{
                            playingNowPage++
                        }
                        playingNowList.emit(it)
                    }
                }
            }else{
                playingNowList.emit(
                    ResponseHandler.Error(message = context.getString(R.string.no_internet_msg))
                )
            }
        }
    }

    fun getPopularList(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            popularList.emit(ResponseHandler.Loading())
            if(context.isOnline()) {
                if(!popularMaxReached) {
                    movieRepository.getMovies(MovieListTypes.Popular, popularPage).collect {
                        if(it.data?.totalPages == popularPage) {
                            popularMaxReached = true
                        }else{
                            popularPage++
                        }
                        popularList.emit(it)
                    }
                }
            }else{
                popularList.emit(
                    ResponseHandler.Error(message = context.getString(R.string.no_internet_msg))
                )
            }
        }
    }

    fun getTopRatedList(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            topRatedList.emit(ResponseHandler.Loading())
            if(context.isOnline()) {
                if(!topRatedMaxReached) {
                    movieRepository.getMovies(MovieListTypes.TopRated, topRatedPage).collect {
                        if(it.data?.totalPages == topRatedPage) {
                            topRatedMaxReached = true
                        }else{
                            topRatedPage++
                        }
                        topRatedList.emit(it)
                    }
                }
            }else{
                topRatedList.emit(
                    ResponseHandler.Error(message = context.getString(R.string.no_internet_msg))
                )
            }
        }
    }

    fun getUpcomingList(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            upcomingList.emit(ResponseHandler.Loading())
            if(context.isOnline()) {
                if(!upcomingMaxReached) {
                    movieRepository.getMovies(MovieListTypes.Upcoming, upcomingPage).collect {
                        if(it.data?.totalPages == upcomingPage) {
                            upcomingMaxReached = true
                        }else{
                            upcomingPage++
                        }
                        upcomingList.emit(it)
                    }
                }
            }else{
                upcomingList.emit(
                    ResponseHandler.Error(message = context.getString(R.string.no_internet_msg))
                )
            }
        }
    }

}