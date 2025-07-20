package com.sonnenstahl.recime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun Loading() {
    val loading = remember { mutableStateOf("Loading") }

    LaunchedEffect(Unit) {
        while (true) {
            if ("..." in loading.value) {
                loading.value = "Loading"
                delay(500L)
                continue
            }
            loading.value += "."
            delay(500L)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(loading.value)
    }
}
