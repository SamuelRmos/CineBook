package com.example.movie.repository

import com.example.movie.model.FilmographyMedia
import com.example.movie.ui.details.DetailRequestState
import kotlinx.coroutines.flow.Flow

interface FilmographyRepository {
    fun getPersonFilmography(personName: String): Flow<DetailRequestState<FilmographyMedia>>
}
