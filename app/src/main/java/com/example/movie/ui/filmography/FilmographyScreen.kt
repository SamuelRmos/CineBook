package com.example.movie.ui.filmography

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movie.R
import com.example.movie.model.Cast
import com.example.movie.model.Constants
import com.example.movie.model.Movie
import com.example.movie.model.placeholderImage
import com.example.movie.model.sampleMovieData
import com.example.movie.navigation.Actions
import com.example.movie.theme.MovieTheme
import com.example.movie.theme.colorAccent
import com.example.movie.theme.colorBackground
import com.example.movie.ui.components.dimensionResource
import com.example.movie.ui.components.shimmerEffect
import com.example.movie.ui.components.textSizeResource
import com.example.movie.ui.state.FilmographyIntent
import com.example.movie.ui.state.FilmographyIntent.LoadFilmography
import com.example.movie.ui.state.FilmographyIntent.SelectTab
import com.example.movie.ui.state.FilmographyState
import com.example.movie.ui.state.FilmographyTab
import com.example.movie.viewmodel.FilmographyViewModel
import com.skydoves.landscapist.coil.CoilImage

private const val GRID_COLUMN_COUNT = 4
private const val SHIMMER_ITEM_COUNT = 16

@Composable
fun FilmographyScreen(
    cast: Cast,
    actions: Actions,
    viewModel: FilmographyViewModel = hiltViewModel()
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()
        }
    }

    LaunchedEffect(cast.name) {
        viewModel.onIntent(LoadFilmography(cast))
    }

    val state by viewModel.state.collectAsState()

    FilmographyScreenContent(
        state = state,
        onIntent = viewModel::onIntent,
        onBackClick = actions.navigateUp,
        onMovieClick = actions.goToMovieDetail
    )
}

@Composable
fun FilmographyScreenContent(
    state: FilmographyState,
    onIntent: (FilmographyIntent) -> Unit,
    onBackClick: () -> Unit,
    onMovieClick: (Movie) -> Unit
) {
    val padding12 = dimensionResource(R.dimen.dimen_12)
    val padding10 = dimensionResource(R.dimen.dimen_10)
    val padding6 = dimensionResource(R.dimen.dimen_6)
    val avatarSize = dimensionResource(R.dimen.dimen_70)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(colorBackground)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Filled.ArrowBack,
                contentDescription = "backIcon",
                tint = Color.White
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding12, vertical = padding6),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val avatarModifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)

            Box(modifier = avatarModifier) {
                CoilImage(
                    modifier = avatarModifier,
                    imageModel = {
                        Constants.artworkDetailImagePoster(state.actorProfilePath.orEmpty())
                    },
                    previewPlaceholder = placeholderImage(state.actorName.hashCode()),
                    success = { imageState ->
                        imageState.drawable?.let { drawable ->
                            val bitmap = remember(drawable) { drawable.toBitmap() }
                            Image(
                                modifier = avatarModifier,
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = state.actorName,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.width(padding12))

            Text(
                text = state.actorName,
                color = Color.White,
                fontSize = textSizeResource(R.dimen.text_size_18),
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            modifier = Modifier.padding(
                start = padding12,
                end = padding12,
                top = padding10,
                bottom = padding6
            ),
            text = stringResource(R.string.filmography_title),
            color = Color.White,
            fontSize = textSizeResource(R.dimen.text_size_16),
            fontWeight = FontWeight.Bold
        )

        val tabs = FilmographyTab.entries
        val selectedIndex = tabs.indexOf(state.selectedTab)

        TabRow(
            selectedTabIndex = selectedIndex,
            containerColor = colorBackground,
            contentColor = Color.White,
            indicator = { tabPositions ->
                if (selectedIndex in tabPositions.indices) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                        color = colorAccent
                    )
                }
            }
        ) {
            tabs.forEach { tab ->
                val tabTitle = when (tab) {
                    FilmographyTab.MOVIE -> stringResource(R.string.tab_movie)
                    FilmographyTab.TV -> stringResource(R.string.tab_tv)
                }
                Tab(
                    selected = state.selectedTab == tab,
                    onClick = { onIntent(SelectTab(tab)) },
                    text = {
                        Text(
                            text = tabTitle,
                            color = Color.White,
                            fontSize = textSizeResource(R.dimen.text_size_14),
                            fontWeight = if (state.selectedTab == tab) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    }
                )
            }
        }

        if (state.isLoading) {
            FilmographyGridShimmer()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(GRID_COLUMN_COUNT),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(padding10),
                horizontalArrangement = Arrangement.spacedBy(padding6),
                verticalArrangement = Arrangement.spacedBy(padding6)
            ) {
                items(
                    items = state.displayedItems,
                    key = { it.id }
                ) { movie ->
                    FilmographyPosterItem(
                        movie = movie,
                        onMovieClick = onMovieClick
                    )
                }
            }
        }
    }
}

@Composable
private fun FilmographyGridShimmer() {
    val padding10 = dimensionResource(R.dimen.dimen_10)
    val padding6 = dimensionResource(R.dimen.dimen_6)
    val posterHeight = dimensionResource(R.dimen.dimen_120)

    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMN_COUNT),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(padding10),
        horizontalArrangement = Arrangement.spacedBy(padding6),
        verticalArrangement = Arrangement.spacedBy(padding6),
        userScrollEnabled = false
    ) {
        items(SHIMMER_ITEM_COUNT) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(posterHeight)
                    .clip(RoundedCornerShape(padding6))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun FilmographyPosterItem(
    movie: Movie,
    onMovieClick: (Movie) -> Unit
) {
    val posterModifier = Modifier
        .fillMaxWidth()
        .height(dimensionResource(R.dimen.dimen_120))
        .clip(RoundedCornerShape(dimensionResource(R.dimen.dimen_6)))
        .clickable { onMovieClick(movie) }

    Box(modifier = posterModifier) {
        CoilImage(
            modifier = posterModifier,
            imageModel = {
                Constants.artworkDetailImagePoster(movie.posterImage.orEmpty())
            },
            previewPlaceholder = placeholderImage(movie.id),
            success = { imageState ->
                imageState.drawable?.let { drawable ->
                    val bitmap = remember(drawable) { drawable.toBitmap() }
                    Image(
                        modifier = posterModifier,
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun FilmographyScreenPreview() {
    MovieTheme {
        Surface(color = colorBackground) {
            FilmographyScreenContent(
                state = FilmographyState(
                    actorName = "Tom Hanks",
                    actorProfilePath = "/tom.jpg",
                    isLoading = false,
                    selectedTab = FilmographyTab.MOVIE,
                    movies = sampleMovieData,
                    tvShows = emptyList()
                ),
                onIntent = {},
                onBackClick = {},
                onMovieClick = {}
            )
        }
    }
}
