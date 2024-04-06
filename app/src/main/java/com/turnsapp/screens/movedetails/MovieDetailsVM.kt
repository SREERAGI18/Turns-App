package com.turnsapp.screens.movedetails

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turnsapp.R
import com.turnsapp.network.ResponseHandler
import com.turnsapp.network.models.moviedetails.MovieDetailsResponse
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
class MovieDetailsVM @Inject constructor(
    private val movieRepository: MovieRepositoryImpl
):ViewModel() {

    val movieDetails = MutableStateFlow<ResponseHandler<MovieDetailsResponse>>(ResponseHandler.Idle("Idle State"))

    fun getMovieDetails(context:Context, movieId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDetails.emit(ResponseHandler.Loading())
            if(context.isOnline()) {
                movieRepository.getMovieDetails(movieId).collect {
                    movieDetails.emit(it)
                }
            }else{
                movieDetails.emit(
                    ResponseHandler.Error(message = context.getString(R.string.no_internet_msg))
                )
            }
        }
    }
}