package com.example.movie.model

import org.junit.Assert.assertEquals
import org.junit.Test

class PersonSearchTest {

    @Test
    fun toMovie_whenMovieItem_mapsTitleAndReleaseDate() {
        val item = KnownForItem(
            id = 13,
            title = "Forrest Gump",
            overview = "Overview",
            posterImage = "/poster.jpg",
            backDropImage = "/backdrop.jpg",
            releaseDate = "1994-06-23",
            mediaType = "movie"
        )

        val movie = item.toMovie()

        assertEquals(13, movie.id)
        assertEquals("Forrest Gump", movie.title)
        assertEquals("1994-06-23", movie.releaseDate)
        assertEquals("/poster.jpg", movie.posterImage)
    }

    @Test
    fun toMovie_whenTvItemWithNameAndFirstAirDate_fallsBackToTvFields() {
        val item = KnownForItem(
            id = 99,
            title = null,
            name = "Band of Brothers",
            overview = "TV Overview",
            posterImage = "/tv.jpg",
            backDropImage = "/tv_bg.jpg",
            releaseDate = null,
            firstAirDate = "2001-09-09",
            mediaType = "tv"
        )

        val movie = item.toMovie()

        assertEquals(99, movie.id)
        assertEquals("Band of Brothers", movie.title)
        assertEquals("2001-09-09", movie.releaseDate)
    }
}
