package com.bakersmath.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.ui.theme.LocalBreadColors
import com.bakersmath.ui.theme.PREVIEW_DARK_BACKGROUND

@Composable
fun FlourInputSection(
    flourInput: String,
    isError: Boolean,
    onFlourChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalBreadColors.current
    val textStyle = MaterialTheme.typography.displayLarge

    Column(modifier = modifier) {
        BasicTextField(
            value = flourInput,
            onValueChange = { raw ->
                val digits = raw.filter { it.isDigit() }
                onFlourChanged(digits)
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Flour weight input"
                    },
            textStyle = textStyle.copy(color = colors.textPrimary, textAlign = TextAlign.End),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // The number is right-aligned and the "g" unit sits flush
                    // against it on the right edge of the screen.
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        if (flourInput.isEmpty()) {
                            Text(
                                text = "0",
                                style = textStyle,
                                color = colors.textSecondary,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        innerTextField()
                    }
                    Text(
                        text = "g",
                        style = textStyle,
                        color = colors.textPrimary,
                    )
                }
            },
        )

        if (isError) {
            Text(
                text = "Max 99,999g",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "FlourInputSection — Light, value", showBackground = true)
@Composable
private fun FlourInputSectionLightPreview() {
    BreadTheme(isDarkMode = false) {
        FlourInputSection(
            flourInput = "500",
            isError = false,
            onFlourChanged = {},
        )
    }
}

@Preview(name = "FlourInputSection — Light, empty + error", showBackground = true)
@Composable
private fun FlourInputSectionErrorPreview() {
    BreadTheme(isDarkMode = false) {
        FlourInputSection(
            flourInput = "",
            isError = true,
            onFlourChanged = {},
        )
    }
}

@Preview(
    name = "FlourInputSection — Dark",
    showBackground = true,
    backgroundColor = PREVIEW_DARK_BACKGROUND,
)
@Composable
private fun FlourInputSectionDarkPreview() {
    BreadTheme(isDarkMode = true) {
        FlourInputSection(
            flourInput = "1200",
            isError = false,
            onFlourChanged = {},
        )
    }
}
