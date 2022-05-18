package com.bongobd.bongotalkies.ui.movie_details

import androidx.lifecycle.LiveData
import com.bongobd.bongotalkies.data.api.MovieApiInterface
import com.bongobd.bongotalkies.data.model.MovieDetails
import com.bongobd.bongotalkies.data.repository.MovieDetailsNetworkDataSource
import com.bongobd.bongotalkies.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: MovieApiInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {

        movieDetailsNetworkDataSource =
            MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }


}