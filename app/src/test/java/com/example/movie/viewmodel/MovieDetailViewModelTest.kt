package com.example.movie.viewmodel

import com.example.movie.model.Cast
import com.example.movie.model.Crew
import com.example.movie.model.MovieCredits
import com.example.movie.model.Video
import com.example.movie.model.VideosList
import com.example.movie.repository.DetailRepository
import com.example.movie.ui.details.DetailRequestState
import com.example.movie.ui.state.MovieDetailIntent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MovieDetailViewModelTest {

    private val repository = mockk<DetailRepository>()

    @Test
    fun onIntent_loadMovieDetails_combinesCreditsAndVideoIntoDetailsState() {
        val cast = listOf(Cast("Actor 1", "/actor1.jpg", "Acting"))
        val credits = MovieCredits(
            cast = cast,
            crew = listOf(Crew("Christopher Nolan", null, "Director"))
        )
        val videos = VideosList(
            results = listOf(
                Video(
                    site = "YouTube",
                    type = "Trailer",
                    key = "trailer_key_1"
                )
            )
        )
        every { repository.getMovieCredits(10) } returns flowOf(
            DetailRequestState.Loading,
            DetailRequestState.Success(credits)
        )
        every { repository.getMovieVideo(10) } returns flowOf(
            DetailRequestState.Loading,
            DetailRequestState.Success(videos)
        )

        val viewModel = MovieDetailViewModel(repository, Dispatchers.Unconfined)
        viewModel.onIntent(MovieDetailIntent.LoadMovieDetails(10))

        val state = viewModel.stateDetails.value
        assertFalse(state.isLoading)
        assertEquals("Christopher Nolan", state.director)
        assertEquals(cast, state.cast)
        assertEquals("trailer_key_1", state.key)
        assertEquals("", state.error)
    }

    @Test
    fun onIntent_loadMovieDetailsWhenPartialError_preservesSuccessDataAndSetsError() {
        val cast = listOf(Cast("Actor 1", "/actor1.jpg", "Acting"))
        val credits = MovieCredits(
            cast = cast,
            crew = listOf(Crew("Christopher Nolan", null, "Director"))
        )
        every { repository.getMovieCredits(10) } returns flowOf(
            DetailRequestState.Success(credits)
        )
        every { repository.getMovieVideo(10) } returns flowOf(
            DetailRequestState.Error("Video failed")
        )

        val viewModel = MovieDetailViewModel(repository, Dispatchers.Unconfined)
        viewModel.onIntent(MovieDetailIntent.LoadMovieDetails(10))

        val state = viewModel.stateDetails.value
        assertFalse(state.isLoading)
        assertEquals("Christopher Nolan", state.director)
        assertEquals(cast, state.cast)
        assertEquals("Video failed", state.error)
    }

    @Test
    fun onIntent_loadMovieDetailsWhenDuplicateMovieId_doesNotRefetch() {
        val credits = MovieCredits(emptyList(), emptyList())
        val videos = VideosList(emptyList())
        every { repository.getMovieCredits(10) } returns flowOf(DetailRequestState.Success(credits))
        every { repository.getMovieVideo(10) } returns flowOf(DetailRequestState.Success(videos))

        val viewModel = MovieDetailViewModel(repository, Dispatchers.Unconfined)
        viewModel.onIntent(MovieDetailIntent.LoadMovieDetails(10))
        viewModel.onIntent(MovieDetailIntent.LoadMovieDetails(10))

        verify(exactly = 1) { repository.getMovieCredits(10) }
        verify(exactly = 1) { repository.getMovieVideo(10) }
    }
}
