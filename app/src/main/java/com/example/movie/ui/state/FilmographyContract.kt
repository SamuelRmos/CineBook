package com.example.movie.ui.state

import com.example.movie.model.Cast
import com.example.movie.model.Movie

enum class FilmographyTab {
    MOVIE,
    TV
}

data class FilmographyState(
    val actorName: String = "",
    val actorProfilePath: String? = null,
    val isLoading: Boolean = false,
    val selectedTab: FilmographyTab = FilmographyTab.MOVIE,
    val movies: List<Movie> = emptyList(),
    val tvShows: List<Movie> = emptyList(),
    val error: String = ""
) {
    val displayedItems: List<Movie>
        get() = when (selectedTab) {
            FilmographyTab.MOVIE -> movies
            FilmographyTab.TV -> tvShows
        }
}

sealed interface FilmographyIntent {
    data class LoadFilmography(val cast: Cast) : FilmographyIntent
    data class SelectTab(val tab: FilmographyTab) : FilmographyIntent
}
