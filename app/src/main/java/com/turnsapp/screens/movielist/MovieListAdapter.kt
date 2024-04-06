package com.turnsapp.screens.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.turnsapp.BuildConfig
import com.turnsapp.R
import com.turnsapp.databinding.ItemMovieBinding
import com.turnsapp.network.models.movielist.Movie


class MovieListAdapter(
    private val onMovieClickListener: OnMovieClickListener
):RecyclerView.Adapter<MovieListAdapter.MovieVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        return MovieVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_movie, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(diff.currentList[position])

        holder.itemView.setOnClickListener {
            onMovieClickListener.onMovieClicked(diff.currentList[position])
        }
    }

    override fun getItemCount(): Int = diff.currentList.size

    fun updateMovies(newList:List<Movie?>) {
        val list = mutableListOf<Movie?>()
        list.addAll(diff.currentList)
        list.addAll(newList)
        diff.submitList(list)
    }

    private val movieDiffUtil = object : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    private val diff = AsyncListDiffer(this, movieDiffUtil)


    class MovieVH(private val binding:ItemMovieBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)

            Glide.with(itemView.context)
                .load(BuildConfig.IMAGE_BASE_URL+movie.posterPath)
                .apply(options)
                .into(binding.moviePoster)
        }
    }

    interface OnMovieClickListener {
        fun onMovieClicked(movie: Movie)
    }
}