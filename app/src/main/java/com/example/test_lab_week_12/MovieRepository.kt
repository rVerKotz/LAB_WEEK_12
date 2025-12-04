package com.example.test_lab_week_12

import androidx.lifecycle.MutableLiveData
import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.model.PopularMoviesResponse
import com.example.test_lab_week_12.api.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val movieService: MovieService) {
    private val apiKey = "24e64bebed4e33fec97d673f70409451"

    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies = movieLiveData

    private val errorLiveData = MutableLiveData<String>()
    val error = errorLiveData

    suspend fun fetchMovies() {
        try {
            val popularMovies = withContext<PopularMoviesResponse>(Dispatchers.IO) {
                movieService.getPopularMovies(apiKey)
            }
            movieLiveData.postValue(popularMovies.results)
        } catch (exception: Exception) {
            errorLiveData.postValue("An error occurred: ${exception.message}")
        }
    }
}
