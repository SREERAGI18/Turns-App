package com.turnsapp.screens.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.turnsapp.R
import com.turnsapp.databinding.FragmentMovieListBinding
import com.turnsapp.network.ResponseHandler
import com.turnsapp.network.models.movielist.Movie
import com.turnsapp.screens.BasicDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [MovieListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val movieListVM:MovieListVM by viewModels()

    private val onMovieClickListener by lazy {
        object : MovieListAdapter.OnMovieClickListener {
            override fun onMovieClicked(movie: Movie) {
                findNavController().navigate(MovieListFragmentDirections.movieListToMovieDetailsScreen(movie.id))
            }
        }
    }

    private val nowPlayingListAdapter by lazy { MovieListAdapter(onMovieClickListener) }
    private val popularListAdapter by lazy { MovieListAdapter(onMovieClickListener) }
    private val topRatedListAdapter by lazy { MovieListAdapter(onMovieClickListener) }
    private val upcomingListAdapter by lazy { MovieListAdapter(onMovieClickListener) }

    private lateinit var binding:FragmentMovieListBinding

    var isErrorDialogShown = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)

        initViews()
        setListeners()
        receiveFlowUpdates()

        getProducts()

        return binding.root
    }

    private fun getProducts() {
        movieListVM.getPlayingNowList(requireContext())
        movieListVM.getPopularList(requireContext())
        movieListVM.getTopRatedList(requireContext())
        movieListVM.getUpcomingList(requireContext())
    }

    private fun initViews() {
        binding.nowPlayingList.adapter = nowPlayingListAdapter
        binding.popularList.adapter = popularListAdapter
        binding.topRatedList.adapter = topRatedListAdapter
        binding.upcomingList.adapter = upcomingListAdapter
    }

    private fun setListeners() {
        binding.nowPlayingList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieListVM.getPlayingNowList(requireContext())
                }
            }
        })

        binding.popularList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieListVM.getPopularList(requireContext())
                }
            }
        })

        binding.topRatedList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieListVM.getTopRatedList(requireContext())
                }
            }
        })

        binding.upcomingList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieListVM.getUpcomingList(requireContext())
                }
            }
        })
    }

    private fun receiveFlowUpdates() {
        lifecycleScope.launch {
            movieListVM.playingNowList.collectLatest { response ->
                binding.progressNowPlaying.visibility = View.GONE
                when(response) {
                    is ResponseHandler.Error -> {

                        response.message?.let { message ->
                            if(!isErrorDialogShown) {
                                isErrorDialogShown = true
                                BasicDialog(
                                    title = "Error",
                                    message = message,
                                    onDismissed = {isErrorDialogShown = false}
                                ).show(childFragmentManager, "Error Dialog")
                            }

                            if(message == getString(R.string.no_internet_msg)) {
//                                binding.txtReload.visibility = View.VISIBLE
                            }
                        }
                    }
                    is ResponseHandler.Idle -> {

                    }
                    is ResponseHandler.Loading -> {
                        binding.progressNowPlaying.visibility = View.VISIBLE
                    }
                    is ResponseHandler.Success -> {
                        response.data?.let {
                            nowPlayingListAdapter.updateMovies(it.movies)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            movieListVM.popularList.collectLatest { response ->
                binding.progressPopular.visibility = View.GONE
                when(response) {
                    is ResponseHandler.Error -> {

                        response.message?.let { message ->
                            if(!isErrorDialogShown) {
                                isErrorDialogShown = true
                                BasicDialog(
                                    title = "Error",
                                    message = message,
                                    onDismissed = {isErrorDialogShown = false}
                                ).show(childFragmentManager, "Error Dialog")

                            }
                            if(message == getString(R.string.no_internet_msg)) {
//                                binding.txtReload.visibility = View.VISIBLE
                            }
                        }
                    }
                    is ResponseHandler.Idle -> {

                    }
                    is ResponseHandler.Loading -> {
                        binding.progressPopular.visibility = View.VISIBLE
                    }
                    is ResponseHandler.Success -> {
                        response.data?.let {
                            popularListAdapter.updateMovies(it.movies)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            movieListVM.topRatedList.collectLatest { response ->
                binding.progressTopRated.visibility = View.GONE
                when(response) {
                    is ResponseHandler.Error -> {

                        response.message?.let { message ->
                            if(!isErrorDialogShown) {
                                isErrorDialogShown = true
                                BasicDialog(
                                    title = "Error",
                                    message = message,
                                    onDismissed = {isErrorDialogShown = false}
                                ).show(childFragmentManager, "Error Dialog")

                            }
                            if(message == getString(R.string.no_internet_msg)) {
//                                binding.txtReload.visibility = View.VISIBLE
                            }
                        }
                    }
                    is ResponseHandler.Idle -> {

                    }
                    is ResponseHandler.Loading -> {
                        binding.progressTopRated.visibility = View.VISIBLE
                    }
                    is ResponseHandler.Success -> {
                        response.data?.let {
                            topRatedListAdapter.updateMovies(it.movies)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            movieListVM.upcomingList.collectLatest { response ->
                binding.progressUpcoming.visibility = View.GONE
                when(response) {
                    is ResponseHandler.Error -> {

                        response.message?.let { message ->
                            if(!isErrorDialogShown) {
                                isErrorDialogShown = true
                                BasicDialog(
                                    title = "Error",
                                    message = message,
                                    onDismissed = {isErrorDialogShown = false}
                                ).show(childFragmentManager, "Error Dialog")

                            }
                            if(message == getString(R.string.no_internet_msg)) {
//                                binding.txtReload.visibility = View.VISIBLE
                            }
                        }
                    }
                    is ResponseHandler.Idle -> {

                    }
                    is ResponseHandler.Loading -> {
                        binding.progressUpcoming.visibility = View.VISIBLE
                    }
                    is ResponseHandler.Success -> {
                        response.data?.let {
                            upcomingListAdapter.updateMovies(it.movies)
                        }
                    }
                }
            }
        }
    }
}