package com.byteberserker.newsboard.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ListItemLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    androidx.compose.material.MaterialTheme {
        androidx.compose.foundation.layout.Column {
            FullScreenLoading()
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
            ListItemLoading()
        }
    }
}
