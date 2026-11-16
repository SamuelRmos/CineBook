package com.example.movie.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonSearchResponse(
    val page: Int = 1,
    val results: List<PersonResult> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PersonResult(
    val id: Int,
    val name: String,
    @Json(name = "profile_path")
    val profilePath: String?,
    @Json(name = "known_for")
    val knownFor: List<KnownForItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class KnownForItem(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    val overview: String? = null,
    @Json(name = "poster_path")
    val posterImage: String? = null,
    @Json(name = "backdrop_path")
    val backDropImage: String? = null,
    @Json(name = "release_date")
    val releaseDate: String? = null,
    @Json(name = "first_air_date")
    val firstAirDate: String? = null,
    @Json(name = "media_type")
    val mediaType: String? = null
) {
    fun toMovie(): Movie = Movie(
        id = id,
        title = title ?: name.orEmpty(),
        overview = overview.orEmpty(),
        posterImage = posterImage,
        backDropImage = backDropImage,
        releaseDate = releaseDate ?: firstAirDate.orEmpty()
    )
}

data class FilmographyMedia(
    val movies: List<Movie> = emptyList(),
    val tvShows: List<Movie> = emptyList()
)
