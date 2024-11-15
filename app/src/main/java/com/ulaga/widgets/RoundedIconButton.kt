package com.ulaga.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val iconButtonSizeModifier = Modifier.size(40.dp)

@Composable
fun RoundedIconButton(
    imageVector: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    elevation: Dp = 4.dp,

    ) {
    Card(modifier = modifier
        .padding(all = 4.dp)
        .background(backgroundColor)
        .wrapContentSize(Alignment.Center)
        .clickable { onClick.invoke() }
        .then(
            iconButtonSizeModifier
        ), shape = CircleShape, colors = CardDefaults.cardColors(
        containerColor = backgroundColor,
    ), elevation = CardDefaults.cardElevation(
        defaultElevation = elevation
    )) {
        Icon(
            modifier = Modifier
                .fillMaxSize(),
            imageVector = imageVector,
            contentDescription = "Plus or minus icon",
            tint = tint,
        )
    }

}