package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    // expose repository LiveData directly
    val popularMovies = movieRepository.movies
    val error = movieRepository.error

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
        }
    }
}
