package com.example.movie.repository

import com.example.movie.model.FilmographyMedia
import com.example.movie.model.KnownForItem
import com.example.movie.model.PersonResult
import com.example.movie.model.PersonSearchResponse
import com.example.movie.network.MovieApi
import com.example.movie.ui.details.DetailRequestState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class FilmographyRepositoryImplTest {

    private val movieApi = mockk<MovieApi>()
    private val repository = FilmographyRepositoryImpl(movieApi)

    private val movieItem = KnownForItem(
        id = 13,
        title = "Forrest Gump",
        overview = "Overview",
        posterImage = "/fg.jpg",
        backDropImage = "/bg.jpg",
        releaseDate = "1994-06-23",
        mediaType = "movie"
    )
    private val tvItem = KnownForItem(
        id = 99,
        name = "Band of Brothers",
        overview = "TV Overview",
        posterImage = "/bob.jpg",
        backDropImage = "/bob_bg.jpg",
        firstAirDate = "2001-09-09",
        mediaType = "tv"
    )

    @Test
    fun getPersonFilmography_whenExactNameMatches_partitionsMoviesAndTvAndDeduplicates(): Unit = runBlocking {
        coEvery { movieApi.searchPerson("tom hanks") } returns Response.success(
            PersonSearchResponse(
                results = listOf(
                    PersonResult(1, "Tom Hanks", "/tom.jpg", listOf(movieItem, movieItem, tvItem)),
                    PersonResult(
                        2,
                        "Tom Hanks Jr",
                        "/jr.jpg",
                        listOf(KnownForItem(id = 777, title = "Other", mediaType = "movie"))
                    )
                )
            )
        )

        val emissions = repository.getPersonFilmography("tom hanks").toList()

        val expected = FilmographyMedia(
            movies = listOf(movieItem.toMovie()),
            tvShows = listOf(tvItem.toMovie())
        )
        assertEquals(
            listOf(DetailRequestState.Loading, DetailRequestState.Success(expected)),
            emissions
        )
    }

    @Test
    fun getPersonFilmography_whenNoExactMatch_fallsBackToAllResults(): Unit = runBlocking {
        coEvery { movieApi.searchPerson("Hanks") } returns Response.success(
            PersonSearchResponse(
                results = listOf(PersonResult(1, "Tom Hanks", "/tom.jpg", listOf(movieItem)))
            )
        )

        val emissions = repository.getPersonFilmography("Hanks").toList()

        val success = emissions[1] as DetailRequestState.Success
        assertEquals(listOf(movieItem.toMovie()), success.data.movies)
    }

    @Test
    fun getPersonFilmography_whenHttpError_emitsLoadingThenError(): Unit = runBlocking {
        val errorBody = "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { movieApi.searchPerson("Tom Hanks") } returns Response.error(500, errorBody)

        val emissions = repository.getPersonFilmography("Tom Hanks").toList()

        assertEquals(DetailRequestState.Loading, emissions[0])
        assertTrue(emissions[1] is DetailRequestState.Error)
    }

    @Test(expected = CancellationException::class)
    fun getPersonFilmography_whenCancelled_rethrowsCancellationException(): Unit = runBlocking {
        coEvery { movieApi.searchPerson("Tom Hanks") } throws CancellationException("Cancelled")

        repository.getPersonFilmography("Tom Hanks").toList()
    }
}
