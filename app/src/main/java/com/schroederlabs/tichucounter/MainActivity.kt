package com.schroederlabs.tichucounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DraggableCirclesWithValues()
        }
    }
}

@Composable
fun DraggableCirclesWithValues() {
    var leftCircleValue by remember { mutableStateOf(0) }
    var rightCircleValue by remember { mutableStateOf(0) }
    var leftCircleOffset by remember { mutableStateOf(0f) }
    var rightCircleOffset by remember { mutableStateOf(0f) }

    val circleRadius = 150f
    val valueChangeAmount = 5

    Box(
        modifier = Modifier.fillMaxSize(), // Fill the screen
        contentAlignment = Alignment.Center // Center the circles horizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Circle (with previous and next value)
            CircleWithValue(
                modifier = Modifier
                    .padding(end = 20.dp) // Padding between the circles
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            leftCircleOffset += dragAmount.y
                            // When dragging upwards (negative dragAmount), increase value
                            if (dragAmount.y < 0) {
                                if (leftCircleValue + 5 <= 100) {
                                    leftCircleValue += valueChangeAmount
                                    rightCircleValue -= valueChangeAmount
                                }
                            }
                            // When dragging downwards (positive dragAmount), decrease value
                            if (dragAmount.y > 0) {
                                if (leftCircleValue - 5 >= -100) {
                                    leftCircleValue -= valueChangeAmount
                                    rightCircleValue += valueChangeAmount
                                }
                            }
                        }
                    },
                circleRadius = circleRadius,
                currentValue = leftCircleValue,
                nextValue = leftCircleValue + valueChangeAmount,
                previousValue = leftCircleValue - valueChangeAmount,
                offset = leftCircleOffset
            )

            // Right Circle (with previous and next value)
            CircleWithValue(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            rightCircleOffset += dragAmount.y
                            // When dragging upwards (negative dragAmount), increase value
                            if (dragAmount.y < 0) {
                                if (rightCircleValue + 5 <= 100) {
                                    rightCircleValue += valueChangeAmount
                                    leftCircleValue -= valueChangeAmount
                                }
                            }
                            // When dragging downwards (positive dragAmount), decrease value
                            if (dragAmount.y > 0) {
                                if (rightCircleValue - 5 >= -100) {
                                    rightCircleValue -= valueChangeAmount
                                    leftCircleValue += valueChangeAmount
                                }
                            }
                        }
                    },
                circleRadius = circleRadius,
                currentValue = rightCircleValue,
                nextValue = rightCircleValue + valueChangeAmount,
                previousValue = rightCircleValue - valueChangeAmount,
                offset = rightCircleOffset
            )
        }
    }
}

@Composable
fun CircleWithValue(
    modifier: Modifier,
    circleRadius: Float,
    currentValue: Int,
    nextValue: Int,
    previousValue: Int,
    offset: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display next value
        Text("Next: $nextValue")

        // Draw the circle and the current value inside it
        Canvas(
            modifier = modifier.size(circleRadius * 2.dp)
        ) {
            val center = center
            drawCircle(
                color = Color.Green,
                radius = circleRadius,
                center = center
            )
            drawContext.canvas.nativeCanvas.drawText(
                "$currentValue", center.x - 20, center.y + 10,
                android.graphics.Paint().apply { color = android.graphics.Color.BLACK; textSize = 50f }
            )
        }

        // Display previous value
        Text("Previous: $previousValue")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DraggableCirclesWithValues()
}
