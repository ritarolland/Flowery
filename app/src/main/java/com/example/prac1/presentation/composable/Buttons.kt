package com.example.prac1.presentation.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.prac1.R

/**
 * Custom filled button with predefined styles
 *
 * @param onClick Callback when button is clicked
 * @param modifier Modifier for styling/layout
 * @param enabled Controls button enabled state
 * @param containerColor Background color of the button
 * @param content Button content/label
 * @author Sofia Bakalskaya
 */
@Composable
fun CustomButtonFilled(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = colorResource(R.color.Primary),
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = colorResource(R.color.Neutral10),
            disabledContentColor = colorResource(R.color.Neutral60)
        ),
        shape = RoundedCornerShape(8.dp),
        enabled = enabled
    ) {
        content()
    }
}

/**
 * Custom outlined button with border and transparent background
 *
 * @param onClick Callback when button is clicked
 * @param modifier Modifier for styling/layout
 * @param paddingValues Padding values for button content
 * @param content Button content/label
 * @author Sofia Bakalskaya
 */
@Composable
fun CustomButtonOutlined(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(12.dp),
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = colorResource(R.color.Primary)
            )
            .defaultMinSize(minWidth = 20.dp, minHeight = 20.dp),
        contentPadding = paddingValues,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = colorResource(R.color.Primary)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        content()
    }
}