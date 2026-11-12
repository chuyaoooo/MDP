package com.example.campuspass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.campuspass.ui.theme.BorderGrey
import com.example.campuspass.ui.theme.Navy
import com.example.campuspass.ui.theme.PlaceholderGrey
import com.example.campuspass.ui.theme.TextPrimary
import com.example.campuspass.ui.theme.TextSecondary
import com.example.campuspass.ui.theme.White

/** Thin grey outline used on every box in the design (cards, inputs, buttons, chips). */
fun Modifier.outlined(): Modifier = this.border(1.dp, BorderGrey)

/**
 * Top app bar: 56dp tall with a grey line underneath.
 * Pass [onBack] to show the "Back" action used on detail screens.
 */
@Composable
fun CampusAppBar(title: String, onBack: (() -> Unit)? = null) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable(role = Role.Button, onClick = onBack)
                )
                Spacer(Modifier.width(18.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderGrey))
    }
}

/** Square outlined text input, 44dp tall, with grey placeholder text. */
@Composable
fun CampusTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        cursorBrush = SolidColor(Navy),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .border(1.dp, if (isError) MaterialTheme.colorScheme.error else BorderGrey),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(placeholder, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
                innerTextField()
            }
        }
    )
}

/** A label above an input, 7dp apart, as in the login form. */
@Composable
fun LabelledField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        content()
    }
}

/** Filled navy button with white text (main action on a screen). */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(if (enabled) Navy else Navy.copy(alpha = 0.4f))
            .outlined()
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = White)
    }
}

/** White outlined button with dark text (secondary actions). */
@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(White)
            .outlined()
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = TextPrimary)
    }
}

/** Filter chip: navy when selected, white with an outline otherwise. */
@Composable
fun CampusFilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(if (selected) Navy else White)
            .outlined()
            .clickable(role = Role.Tab, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) White else TextPrimary
        )
    }
}

/** Grey box standing in for an event photo until real images are added. */
@Composable
fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Box(modifier.background(PlaceholderGrey))
}

/**
 * Event card used in the marketplace: image on the left, then title and detail lines.
 * [details] are the smaller lines under the title (date, points, eligibility...).
 */
@Composable
fun EventCard(
    title: String,
    details: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .outlined()
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ImagePlaceholder(Modifier.size(width = 64.dp, height = 78.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            details.forEach { line ->
                Text(line, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

/** One destination in the student bottom bar. */
data class BottomBarItem(val route: String, val label: String)

/**
 * Text-only bottom navigation from the Figma: equal-width items,
 * the selected one filled navy with bold white text.
 */
@Composable
fun CampusBottomBar(
    items: List<BottomBarItem>,
    selectedRoute: String?,
    onSelect: (BottomBarItem) -> Unit
) {
    Column(Modifier.background(White)) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderGrey))
        Row(Modifier.fillMaxWidth().height(56.dp)) {
            items.forEach { item ->
                val selected = item.route == selectedRoute
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(if (selected) Navy else White)
                        .clickable(role = Role.Tab) { onSelect(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) White else TextPrimary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/** Temporary screen for tabs that have not been built yet. */
@Composable
fun PlaceholderScreen(title: String) {
    Column {
        CampusAppBar(title)
        Box(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("$title screen coming soon", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}
