package com.example.movie.viewmodel

import com.example.movie.model.Cast
import com.example.movie.model.FilmographyMedia
import com.example.movie.model.Movie
import com.example.movie.repository.FilmographyRepository
import com.example.movie.ui.details.DetailRequestState
import com.example.movie.ui.state.FilmographyIntent
import com.example.movie.ui.state.FilmographyTab
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class FilmographyViewModelTest {

    private val repository = mockk<FilmographyRepository>()
    private val cast = Cast(name = "Tom Hanks", imageProfile = "/tom.jpg", job = "Acting")
    private val movie = Movie(13, "Forrest Gump", "Overview", "/fg.jpg", "/bg.jpg", "1994-06-23")
    private val tvShow = Movie(99, "Band of Brothers", "TV Overview", "/bob.jpg", "/bg2.jpg", "2001-09-09")

    @Test
    fun onIntent_loadFilmography_reducesSuccessAndSelectTabSwitchesDisplayedItems() {
        every { repository.getPersonFilmography("Tom Hanks") } returns flowOf(
            DetailRequestState.Loading,
            DetailRequestState.Success(FilmographyMedia(movies = listOf(movie), tvShows = listOf(tvShow)))
        )

        val viewModel = FilmographyViewModel(repository, Dispatchers.Unconfined)
        viewModel.onIntent(FilmographyIntent.LoadFilmography(cast))

        val movieTabState = viewModel.state.value
        assertEquals("Tom Hanks", movieTabState.actorName)
        assertEquals("/tom.jpg", movieTabState.actorProfilePath)
        assertFalse(movieTabState.isLoading)
        assertEquals(FilmographyTab.MOVIE, movieTabState.selectedTab)
        assertEquals(listOf(movie), movieTabState.displayedItems)

        viewModel.onIntent(FilmographyIntent.SelectTab(FilmographyTab.TV))
        val tvTabState = viewModel.state.value
        assertEquals(FilmographyTab.TV, tvTabState.selectedTab)
        assertEquals(listOf(tvShow), tvTabState.displayedItems)
    }

    @Test
    fun onIntent_loadFilmographyWhenDuplicateActor_doesNotRefetch() {
        every { repository.getPersonFilmography("Tom Hanks") } returns flowOf(
            DetailRequestState.Loading,
            DetailRequestState.Success(FilmographyMedia(movies = listOf(movie), tvShows = listOf(tvShow)))
        )

        val viewModel = FilmographyViewModel(repository, Dispatchers.Unconfined)
        viewModel.onIntent(FilmographyIntent.LoadFilmography(cast))
        viewModel.onIntent(FilmographyIntent.LoadFilmography(cast))

        verify(exactly = 1) { repository.getPersonFilmography("Tom Hanks") }
    }

    @Test
    fun onIntent_loadFilmographyWhenError_updatesErrorState() {
        every { repository.getPersonFilmography("Tom Hanks") } returns flowOf(
            DetailRequestState.Loading,
            DetailRequestState.Error("Network error")
        )

        val viewModel = FilmographyViewModel(repository, Dispatchers.Unconfined)
        viewModel.onIntent(FilmographyIntent.LoadFilmography(cast))

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
    }
}
