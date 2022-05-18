package com.bongobd.bongotalkies.data.api

import com.bongobd.bongotalkies.data.model.MovieDetails
import com.bongobd.bongotalkies.data.model.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface {
    @GET("movie/top_rated")
    fun getTopRated(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}