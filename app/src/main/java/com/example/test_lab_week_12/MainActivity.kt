package com.example.test_lab_week_12

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_lab_week_12.model.Movie
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), MovieAdapter.MovieClickListener {

    private val movieAdapter = MovieAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.movie_list)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository

        val movieViewModel = ViewModelProvider(
            this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MovieViewModel(movieRepository) as T
                }
            }
        )[MovieViewModel::class.java]

        movieViewModel.popularMovies.observe(this) { popularMovies ->
            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString()

            movieAdapter.addMovies(
                popularMovies
                    .filter { movie -> movie.releaseDate?.startsWith(currentYear) == true }
                    .sortedByDescending { it.popularity }
            )
        }

        movieViewModel.error.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}
