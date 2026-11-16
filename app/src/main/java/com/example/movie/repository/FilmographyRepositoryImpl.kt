package com.example.movie.repository

import com.example.movie.model.FilmographyMedia
import com.example.movie.network.MovieApi
import com.example.movie.ui.details.DetailRequestState
import com.example.movie.ui.details.DetailRequestState.Error
import com.example.movie.ui.details.DetailRequestState.Loading
import com.example.movie.ui.details.DetailRequestState.Success
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

private const val MEDIA_TYPE_MOVIE = "movie"
private const val MEDIA_TYPE_TV = "tv"
private const val FILMOGRAPHY_ERROR_MESSAGE = "Error while loading filmography"

class FilmographyRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : FilmographyRepository {

    override fun getPersonFilmography(personName: String): Flow<DetailRequestState<FilmographyMedia>> = flow {
        emit(Loading)
        try {
            val response = movieApi.searchPerson(query = personName)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val matchedPeople = body.results
                    .filter { it.name.equals(personName, ignoreCase = true) }
                    .ifEmpty { body.results }

                val knownForItems = matchedPeople.flatMap { it.knownFor }

                val movies = knownForItems
                    .filter { it.mediaType == MEDIA_TYPE_MOVIE || (it.mediaType == null && it.title != null) }
                    .distinctBy { it.id }
                    .map { it.toMovie() }

                val tvShows = knownForItems
                    .filter { it.mediaType == MEDIA_TYPE_TV }
                    .distinctBy { it.id }
                    .map { it.toMovie() }

                emit(Success(FilmographyMedia(movies = movies, tvShows = tvShows)))
            } else {
                emit(Error(FILMOGRAPHY_ERROR_MESSAGE))
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Timber.e(exception)
            emit(Error(FILMOGRAPHY_ERROR_MESSAGE))
        }
    }.flowOn(Dispatchers.IO)
}
