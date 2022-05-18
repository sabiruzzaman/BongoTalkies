package com.bongobd.bongotalkies.ui.movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bongobd.bongotalkies.data.api.MovieApiClient
import com.bongobd.bongotalkies.data.api.MovieApiInterface
import com.bongobd.bongotalkies.data.api.POSTER_BASE_URL
import com.bongobd.bongotalkies.data.model.MovieDetails
import com.bongobd.bongotalkies.data.repository.NetworkState
import com.bongobd.bongotalkies.databinding.ActivityMovieDetailsBinding
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var detailsViewModel: MovieDetailsViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewInit()
    }

    private fun viewInit() {
        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: MovieApiInterface = MovieApiClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        detailsViewModel = getViewModel(movieId)

        detailsViewModel.movieDetailsDetails.observe(this, Observer {
            bindUI(it)
        })

        detailsViewModel.networkState.observe(this, Observer {
            binding.progressBar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })


        binding.closeLayout.setOnClickListener() {
            finish()
        }
    }

    fun bindUI(it: MovieDetails) {
        binding.movieTitle.text = it.title
        binding.movieTagline.text = it.tagline
        binding.movieReleaseDate.text = it.releaseDate
        binding.movieRating.text = it.rating.toString()
        binding.movieRuntime.text = it.runtime.toString() + " minutes"
        binding.movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        binding.movieBudget.text = formatCurrency.format(it.budget)
        binding.movieRevenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(binding.ivMoviePoster);

    }

    private fun getViewModel(movieId: Int): MovieDetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailsViewModel(movieRepository, movieId) as T
            }
        })[MovieDetailsViewModel::class.java]
    }
}
