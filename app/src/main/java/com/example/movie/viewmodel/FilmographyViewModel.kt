package com.example.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.Cast
import com.example.movie.repository.FilmographyRepository
import com.example.movie.ui.details.DetailRequestState.Error
import com.example.movie.ui.details.DetailRequestState.Loading
import com.example.movie.ui.details.DetailRequestState.Success
import com.example.movie.ui.state.FilmographyIntent
import com.example.movie.ui.state.FilmographyIntent.LoadFilmography
import com.example.movie.ui.state.FilmographyIntent.SelectTab
import com.example.movie.ui.state.FilmographyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmographyViewModel(
    private val filmographyRepository: FilmographyRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    @Inject
    constructor(
        filmographyRepository: FilmographyRepository
    ) : this(filmographyRepository, Dispatchers.IO)

    private val _state = MutableStateFlow(FilmographyState())
    val state: StateFlow<FilmographyState> = _state.asStateFlow()

    private var loadedActorName: String? = null

    fun onIntent(intent: FilmographyIntent) {
        when (intent) {
            is LoadFilmography -> loadFilmography(intent.cast)
            is SelectTab -> _state.update { it.copy(selectedTab = intent.tab) }
        }
    }

    private fun loadFilmography(cast: Cast) {
        if (loadedActorName == cast.name && (_state.value.isLoading || _state.value.error.isEmpty())) {
            return
        }
        loadedActorName = cast.name
        _state.update {
            it.copy(
                actorName = cast.name,
                actorProfilePath = cast.imageProfile,
                isLoading = true,
                error = ""
            )
        }
        viewModelScope.launch(ioDispatcher) {
            filmographyRepository.getPersonFilmography(cast.name).collect { requestState ->
                when (requestState) {
                    is Loading -> _state.update {
                        it.copy(isLoading = true, error = "")
                    }
                    is Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            movies = requestState.data.movies,
                            tvShows = requestState.data.tvShows,
                            error = ""
                        )
                    }
                    is Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            error = requestState.message
                        )
                    }
                }
            }
        }
    }
}
