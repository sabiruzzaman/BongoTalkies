package com.bongobd.bongotalkies.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.bongobd.bongotalkies.data.api.MovieApiInterface
import com.bongobd.bongotalkies.data.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val apiService: MovieApiInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}