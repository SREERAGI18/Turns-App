package com.turnsapp.screens.movedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.turnsapp.BuildConfig
import com.turnsapp.R
import com.turnsapp.databinding.FragmentMovieDetailsBinding
import com.turnsapp.network.ResponseHandler
import com.turnsapp.network.models.moviedetails.MovieDetailsResponse
import com.turnsapp.screens.BasicDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    lateinit var binding: FragmentMovieDetailsBinding
    private val viewModel: MovieDetailsVM by viewModels()
    private val args by navArgs<MovieDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)

        getMovieDetails()
        setListeners()
        receiveFlowUpdates()

        return binding.root
    }

    private fun setListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getMovieDetails() {
        viewModel.getMovieDetails(
            context = requireContext(),
            movieId = args.movieId
        )
    }

    private fun receiveFlowUpdates() {
        lifecycleScope.launch {
            viewModel.movieDetails.collectLatest { response ->
                binding.progress.visibility = View.GONE
                when (response) {
                    is ResponseHandler.Error -> {

                        response.message?.let { message ->
                            BasicDialog(
                                title = "Error",
                                message = message,
                            ).show(childFragmentManager, "Error Dialog")

                            if (message == getString(R.string.no_internet_msg)) {
//                                binding.txtReload.visibility = View.VISIBLE
                            }
                        }
                    }

                    is ResponseHandler.Idle -> {

                    }

                    is ResponseHandler.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                    }

                    is ResponseHandler.Success -> {
                        response.data?.let {
                            bindData(it)
                        }
                    }
                }
            }
        }
    }

    private fun bindData(movieDetailsResponse: MovieDetailsResponse) {

        binding.movieTitle.text = movieDetailsResponse.title
        binding.movieReleaseYear.text = movieDetailsResponse.releaseDate.split("-")[0]
        binding.movieDuration.text = "${movieDetailsResponse.runtime}m"
        binding.movieDescription.text = movieDetailsResponse.overview

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.no_image)
            .error(R.mipmap.no_image)

        Glide.with(requireContext())
            .load(BuildConfig.IMAGE_BASE_URL+movieDetailsResponse.posterPath)
            .apply(options)
            .into(binding.moviePreview)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MovieDetailsFragment()
    }
}