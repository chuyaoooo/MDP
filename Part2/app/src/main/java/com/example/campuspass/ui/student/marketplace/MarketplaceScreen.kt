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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campuspass.data.repository.SampleEventRepository
import com.example.campuspass.ui.components.CampusAppBar
import com.example.campuspass.ui.components.CampusFilterChip
import com.example.campuspass.ui.components.CampusTextField
import com.example.campuspass.ui.components.EventCard
import com.example.campuspass.ui.components.SecondaryButton
import com.example.campuspass.ui.components.TextAction
import com.example.campuspass.ui.components.outlined
import com.example.campuspass.ui.theme.CampusPassTheme
import com.example.campuspass.ui.theme.Navy
import com.example.campuspass.ui.theme.SystemGrey
import com.example.campuspass.ui.theme.TextPrimary
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
        actions = MarketplaceActions(
            onQueryChange = viewModel::onQueryChange,
            onFilterSelected = viewModel::onFilterSelected,
            onOpenCoursePicker = viewModel::onOpenCoursePicker,
            onDismissCoursePicker = viewModel::onDismissCoursePicker,
            onCourseSelected = viewModel::onCourseSelected,
            onClearCourse = viewModel::onClearCourse,
            onEventClick = onEventClick
        )
    )
}

/** Everything the user can do on this screen, grouped to keep the screen's parameter list short. */
data class MarketplaceActions(
    val onQueryChange: (String) -> Unit = {},
    val onFilterSelected: (MarketplaceFilter) -> Unit = {},
    val onOpenCoursePicker: () -> Unit = {},
    val onDismissCoursePicker: () -> Unit = {},
    val onCourseSelected: (String) -> Unit = {},
    val onClearCourse: () -> Unit = {},
    val onEventClick: (String) -> Unit = {}
)

/**
 * Figma frames "02 Discover events" (no course filter) and "03 Filter by course" (course filter applied).
 */
@Composable
fun MarketplaceScreen(state: MarketplaceUiState, actions: MarketplaceActions) {
    val focusManager = LocalFocusManager.current
    val courseApplied = state.selectedCourse != null

    Column(Modifier.fillMaxSize().background(White)) {
        CampusAppBar(title = "CampusPass")

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Figma spaces the header 16dp apart, tightening to 12dp when the course filter box is shown
                Column(
                    modifier = Modifier.padding(bottom = if (courseApplied) 0.dp else 4.dp),
                    verticalArrangement = Arrangement.spacedBy(if (courseApplied) 12.dp else 16.dp)
                ) {
                    Text("Discover events", style = MaterialTheme.typography.headlineSmall)

                    CampusTextField(
                        value = state.query,
                        onValueChange = actions.onQueryChange,
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
                                onClick = { actions.onFilterSelected(filter) }
                            )
                        }
                    }

                    if (state.selectedCourse != null) {
                        AppliedCourseFilter(
                            course = state.selectedCourse,
                            onClear = actions.onClearCourse,
                            onChange = actions.onOpenCoursePicker
                        )
                    } else {
                        // Entry point to the course filter (not drawn in Figma 02, which only shows the result)
                        TextAction(text = "Filter by course", onClick = actions.onOpenCoursePicker)
                    }

                    Text(state.resultsLabel, style = MaterialTheme.typography.titleSmall)
                }
            }

            if (!state.isLoading && state.events.isEmpty()) {
                item {
                    Text(
                        text = if (courseApplied) "No events award s-CPD points for this course right now. Try another filter or clear the course."
                        else "No events match your search. Try another word or filter.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            items(state.events, key = { it.id }) { event ->
                EventCard(
                    title = event.title,
                    details = listOf(event.dateLine, event.pointsLine),
                    highlight = event.eligibilityLine,
                    onClick = { actions.onEventClick(event.id) }
                )
            }
        }
    }

    if (state.isCoursePickerOpen) {
        CoursePickerDialog(
            courses = state.courseOptions,
            selectedCourse = state.selectedCourse,
            onSelect = actions.onCourseSelected,
            onDismiss = actions.onDismissCoursePicker
        )
    }
}

/** Grey box from Figma 03 showing which course the list is filtered by. */
@Composable
private fun AppliedCourseFilter(course: String, onClear: () -> Unit, onChange: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SystemGrey)
            .outlined()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "s-CPD course filter · Applied",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            TextAction(text = "Clear", onClick = onClear)
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(course, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            TextAction(text = "Change", onClick = onChange)
        }
        Text(
            text = "Only events awarding s-CPD points for this course.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

/** Simple course list in the app's square, outlined style. */
@Composable
private fun CoursePickerDialog(
    courses: List<String>,
    selectedCourse: String?,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .outlined()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Filter by course", style = MaterialTheme.typography.titleLarge)
            Text(
                "Show only events that award s-CPD points for this course.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Column(Modifier.verticalScroll(rememberScrollState())) {
                courses.forEach { course ->
                    val selected = course == selectedCourse
                    Text(
                        text = course,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selected) White else TextPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (selected) Navy else White)
                            .selectable(selected = selected, role = Role.RadioButton) { onSelect(course) }
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                    )
                }
            }
            SecondaryButton(text = "Cancel", onClick = onDismiss)
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 659)
@Composable
private fun MarketplaceScreenPreview() {
    CampusPassTheme {
        MarketplaceScreen(
            state = MarketplaceUiState(
                events = SampleEventRepository.events.take(3).map { MarketplaceViewModel.toCardUi(it) },
                isLoading = false
            ),
            actions = MarketplaceActions()
        )
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 659, name = "Filtered by course")
@Composable
private fun MarketplaceFilteredPreview() {
    val course = "BA Business Management"
    CampusPassTheme {
        MarketplaceScreen(
            state = MarketplaceUiState(
                selectedCourse = course,
                events = SampleEventRepository.events
                    .filter { it.qualifiesFor(course) }
                    .map { MarketplaceViewModel.toCardUi(it, course) },
                isLoading = false
            ),
            actions = MarketplaceActions()
        )
    }
}
