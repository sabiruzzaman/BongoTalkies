package com.bongobd.bongotalkies.ui.top_rated_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bongobd.bongotalkies.R
import com.bongobd.bongotalkies.data.api.POSTER_BASE_URL
import com.bongobd.bongotalkies.data.model.Movie
import com.bongobd.bongotalkies.data.model.MovieDetails
import com.bongobd.bongotalkies.data.repository.NetworkState
import com.bongobd.bongotalkies.databinding.MovieListItemBinding
import com.bongobd.bongotalkies.databinding.NetworkStateItemBinding
import com.bongobd.bongotalkies.ui.movie_details.MovieDetailsActivity
import com.bumptech.glide.Glide

class TopRatedMoviePagedListAdapter(val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding = MovieListItemBinding.bind(itemView)


        fun bind(movie: Movie?, context: Context) {


            binding.cvMovieTitle.text = movie?.title
            binding.cvMovieReleaseDate.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(binding.cvIvMoviePoster);

            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }

        }

    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding = NetworkStateItemBinding.bind(itemView)
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                binding.progressBarItem.visibility = View.VISIBLE;
            } else {
                binding.progressBarItem.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                binding.errorMsgItem.visibility = View.VISIBLE;
                binding.errorMsgItem.text = networkState.msg;
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                binding.errorMsgItem.visibility = View.VISIBLE;
                binding.errorMsgItem.text = networkState.msg;
            } else {
                binding.errorMsgItem.visibility = View.GONE;
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }


}