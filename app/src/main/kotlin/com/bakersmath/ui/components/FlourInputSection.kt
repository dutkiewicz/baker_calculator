package com.bakersmath.ui.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.bakersmath.ui.theme.BreadTheme
import com.bakersmath.ui.theme.LocalBreadColors
import androidx.compose.foundation.layout.Column

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
            modifier = Modifier.semantics {
                contentDescription = "Flour weight input"
            },
            textStyle = textStyle.copy(color = colors.textPrimary),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (flourInput.isEmpty()) {
                    Text(
                        text = "0",
                        style = textStyle,
                        color = colors.textSecondary,
                    )
                } else {
                    // Render the live text with the "g" suffix inline at the same size
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = colors.textPrimary)) {
                                append(flourInput)
                            }
                            withStyle(SpanStyle(color = colors.textPrimary)) {
                                append("g")
                            }
                        },
                        style = textStyle,
                    )
                }
                // Keep the real cursor/input active (zero-width overlay)
                innerTextField()
            },
        )

        if (isError) {
            Text(
                text = "Max 99,999g",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
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

@Preview(name = "FlourInputSection — Dark", showBackground = true, backgroundColor = 0xFF1E1108)
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
