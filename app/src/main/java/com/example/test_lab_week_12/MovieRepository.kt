package com.example.test_lab_week_12

import com.example.test_lab_week_12.model.Movie
import com.example.test_lab_week_12.api.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class MovieRepository(private val movieService: MovieService) {
    private val apiKey = "24e64bebed4e33fec97d673f70409451"
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    suspend fun fetchMovies() {
        try {
            val result = withContext(Dispatchers.IO) {
                movieService.getPopularMovies(apiKey)
            }
            _movies.emit(result.results)
        } catch (exception: Exception) {
            _error.emit("An error occurred: ${exception.message}")
        }
    }
}
