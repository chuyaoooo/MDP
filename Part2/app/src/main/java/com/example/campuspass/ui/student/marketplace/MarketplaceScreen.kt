package com.example.campuspass.ui.student.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuspass.data.repository.SampleEventRepository
import com.example.campuspass.ui.components.CampusAppBar
import com.example.campuspass.ui.components.CampusFilterChip
import com.example.campuspass.ui.components.CampusTextField
import com.example.campuspass.ui.components.EventCard
import com.example.campuspass.ui.theme.CampusPassTheme
import com.example.campuspass.ui.theme.TextSecondary
import com.example.campuspass.ui.theme.White

/** Connects the marketplace screen to its ViewModel. */
@Composable
fun MarketplaceRoute(
    onEventClick: (String) -> Unit,
    viewModel: MarketplaceViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    MarketplaceScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onFilterSelected = viewModel::onFilterSelected,
        onEventClick = onEventClick
    )
}

/** Figma frame "02 Discover events". */
@Composable
fun MarketplaceScreen(
    state: MarketplaceUiState,
    onQueryChange: (String) -> Unit,
    onFilterSelected: (MarketplaceFilter) -> Unit,
    onEventClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(Modifier.fillMaxSize().background(White)) {
        CampusAppBar(title = "CampusPass")

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Heading, search, chips and result count are spaced 16dp apart in Figma
            item {
                Column(
                    modifier = Modifier.padding(bottom = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Discover events", style = MaterialTheme.typography.headlineSmall)

                    CampusTextField(
                        value = state.query,
                        onValueChange = onQueryChange,
                        placeholder = "Search events",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MarketplaceFilter.entries.forEach { filter ->
                            CampusFilterChip(
                                label = filter.label,
                                selected = filter == state.selectedFilter,
                                onClick = { onFilterSelected(filter) }
                            )
                        }
                    }

                    Text(state.resultsLabel, style = MaterialTheme.typography.titleSmall)
                }
            }

            if (!state.isLoading && state.events.isEmpty()) {
                item {
                    Text(
                        text = "No events match your search. Try another word or filter.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            items(state.events, key = { it.id }) { event ->
                EventCard(
                    title = event.title,
                    details = listOf(event.dateLine, event.pointsLine),
                    onClick = { onEventClick(event.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 659)
@Composable
private fun MarketplaceScreenPreview() {
    CampusPassTheme {
        MarketplaceScreen(
            state = MarketplaceUiState(
                events = SampleEventRepository.events.take(3).map(MarketplaceViewModel::toCardUi),
                isLoading = false
            ),
            onQueryChange = {}, onFilterSelected = {}, onEventClick = {}
        )
    }
}
