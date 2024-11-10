package com.schroederlabs.tichucounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.times

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CenteredCircleWithValues()
        }
    }
}

@Composable
fun CenteredCircleWithValues() {
    var topValue by remember { mutableStateOf(0) }
    var bottomValue by remember { mutableStateOf(0) }

    val circleRadius = 250f

    // Box to center the circle on the screen
    Box(
        modifier = Modifier.fillMaxSize(), // Fill the entire screen
        contentAlignment = Alignment.Center // Center the content within the Box
    ) {
        // Circle drawing and click handling logic
        Canvas(modifier = Modifier
            .size(circleRadius * 1.dp) // Set the size of the Canvas to twice the radius (diameter)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val topHalfHeight = circleRadius // Checking if the tap is on the top half
                    if (offset.y < topHalfHeight) {
                        // Top half tapped, increase topValue and decrease bottomValue
                        topValue += 5
                        bottomValue -= 5
                    }
                    else if (offset.y > topValue) {
                        // Bottom half tapped, increase bottomValue and decrease topValue
                        topValue -= 5
                        bottomValue += 5
                    }
                }
            }
        ) {
            val center = center
            // Draw the top half of the circle
            drawArc(
                color = Color.Green,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                size = androidx.compose.ui.geometry.Size(circleRadius * 2, circleRadius * 2),
                topLeft = center.copy(x = center.x - circleRadius, y = center.y - circleRadius)
            )

            // Draw the bottom half of the circle
            drawArc(
                color = Color.Red,
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = true,
                size = androidx.compose.ui.geometry.Size(circleRadius * 2, circleRadius * 2),
                topLeft = center.copy(x = center.x - circleRadius, y = center.y - circleRadius)
            )

            // Display values on top and bottom
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    "Top: $topValue", center.x - 40, center.y - circleRadius + 50f,
                    android.graphics.Paint().apply { color = android.graphics.Color.BLACK; textSize = 30f }
                )
                canvas.nativeCanvas.drawText(
                    "Bottom: $bottomValue", center.x - 50, center.y + circleRadius - 20f,
                    android.graphics.Paint().apply { color = android.graphics.Color.BLACK; textSize = 30f }
                )
            }
        }

        // Below the canvas, show buttons for additional interactions (optional)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Top value: $topValue")
            Text("Bottom value: $bottomValue")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CenteredCircleWithValues()
}