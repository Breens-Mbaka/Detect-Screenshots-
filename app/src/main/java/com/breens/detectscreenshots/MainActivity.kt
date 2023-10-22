package com.breens.detectscreenshots

import android.annotation.SuppressLint
import android.app.Activity.ScreenCaptureCallback
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.breens.detectscreenshots.ui.theme.DetectScreenShotsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    private val screenCaptureCallback = ScreenCaptureCallback {
        mainActivityViewModel.showSnackBar("Screenshot detected 📸👀")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val uiEvents = mainActivityViewModel.uiEvents

            LaunchedEffect(key1 = true) {
                uiEvents.collect { uiEvent ->
                    when (uiEvent) {
                        is UiEvents.ShowSnackBar -> {
                            snackBarHostState.showSnackbar(
                                message = uiEvent.message,
                                withDismissAction = true,
                                duration = SnackbarDuration.Long,
                            )
                        }
                    }
                }
            }

            DetectScreenShotsTheme {
                Scaffold(
                    snackbarHost = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            SnackbarHost(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.TopCenter),
                                hostState = snackBarHostState,
                            )
                        }
                    },
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Detect Screenshots 📸 Tutorial")
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    override fun onStart() {
        super.onStart()
        registerScreenCaptureCallback(mainExecutor, screenCaptureCallback)
    }

    @SuppressLint("NewApi")
    override fun onStop() {
        super.onStop()
        unregisterScreenCaptureCallback(screenCaptureCallback)
    }
}
