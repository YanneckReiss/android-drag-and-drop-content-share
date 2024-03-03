@file:OptIn(ExperimentalFoundationApi::class)

package com.yanneckreiss.contentreceiveapp.ui.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yanneckreiss.contentreceiveapp.ui.theme.AndroidTutorialTemplateTheme

private const val TEXT_MIME_TYPE = "text/"

@Composable
fun ReceiveContentScreen(
    viewModel: ReceiveContentViewModel = viewModel { ReceiveContentViewModel() }
) {

    val state: ReceiveContentState by viewModel.state.collectAsStateWithLifecycle()

    ReceiveContentScreenContent(
        text = state.text,
        onContentDropped = { updatedText ->
            viewModel.setDroppedContent(updatedText.toAndroidDragEvent().clipData)
        }
    )
}

@Composable
private fun ReceiveContentScreenContent(
    text: String,
    onContentDropped: (DragAndDropEvent) -> Unit
) {

    var shouldShowDropZone: Boolean by remember { mutableStateOf(false) }
    val animatedOpacity: Float by animateFloatAsState(
        targetValue = if (shouldShowDropZone) 1.0f else 0.0f,
        label = "AnimatedOpacity"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .dragAndDropTarget(
                shouldStartDragAndDrop = accept@{ startEvent ->
                    return@accept startEvent
                        .mimeTypes()
                        .any<String> { eventMimeType -> eventMimeType.startsWith(TEXT_MIME_TYPE) }
                },
                target = object : DragAndDropTarget {

                    override fun onDrop(event: DragAndDropEvent): Boolean {
                        Log.d("ReceiveContentScreenContent", "---> onDrop, ${event.toAndroidDragEvent()}")
                        onContentDropped(event)
                        return true
                    }

                    override fun onStarted(event: DragAndDropEvent) {
                        Log.d("ReceiveContentScreenContent", "---> onStarted, ${event.toAndroidDragEvent()}")
                        shouldShowDropZone = true
                    }

                    override fun onEnded(event: DragAndDropEvent) {
                        Log.d("ReceiveContentScreenContent", "---> onEnded, ${event.toAndroidDragEvent()}")
                        shouldShowDropZone = false
                    }
                }
            )
            .systemBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "\uD83D\uDCC1 Drop your content down below",
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (text.isBlank()) {
            Spacer(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = animatedOpacity),
                        RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text
        )
    }
}

@Composable
@Preview
private fun Preview_ReceiveContentScreen() {
    AndroidTutorialTemplateTheme {
        ReceiveContentScreenContent(
            text = "hello",
            onContentDropped = {}
        )
    }
}