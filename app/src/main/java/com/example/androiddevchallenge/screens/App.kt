/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.androiddevchallenge.ui.theme.progressGradient
import com.example.androiddevchallenge.ui.theme.purple700
import com.example.androiddevchallenge.ui.theme.titleTextStyle
import com.example.androiddevchallenge.viewmodel.CounterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

val colorIndex = mutableStateOf(0)

@ExperimentalAnimationApi
@Composable
fun App(counterViewModel: CounterViewModel, lifecycleScope: LifecycleCoroutineScope) {
    Scaffold(
        topBar = {
            Text(
                text = "JetTimer", style = titleTextStyle,
                modifier = Modifier
                    .fillMaxWidth().offset(y = 20.dp)
                    .height(80.dp),
                textAlign = TextAlign.Center
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val editVisibilityState = remember { mutableStateOf(true) }
                Card(
                    modifier = Modifier.size(250.dp),
                    elevation = if (counterViewModel.editState.value) 0.dp else 16.dp,
                    shape = CircleShape
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AnimatedCircle(
                            modifier = Modifier.fillMaxSize(),
                            counterViewModel.editState,
                            sweepAngleState = counterViewModel.sweepAngleState,
                            lifecycleScope = lifecycleScope
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HourPicker(counterViewModel = counterViewModel)
                            Spacer(modifier = Modifier.width(if (counterViewModel.editState.value) 8.dp else 0.dp))
                            if (!counterViewModel.editState.value) {
                                Text(text = " : ", style = MaterialTheme.typography.h3)
                            }
                            MinutePicker(counterViewModel = counterViewModel)
                            Spacer(modifier = Modifier.width(if (counterViewModel.editState.value) 8.dp else 0.dp))
                            SecondPicker(counterViewModel = counterViewModel)
                        }
                    }
                }
                val icon =
                    if (counterViewModel.editState.value) Icons.Filled.PlayArrow else Icons.Filled.Stop
                Spacer(modifier = Modifier.height(24.dp))
                AnimatedVisibility(
                    visible = editVisibilityState.value,
                    enter = fadeIn(animationSpec = tween(200, easing = FastOutLinearInEasing)),
                    exit = fadeOut(animationSpec = tween(200, easing = LinearOutSlowInEasing))
                ) {
                    IconButton(
                        onClick = {
                            lifecycleScope.launchWhenStarted {
                                editVisibilityState.value = false
                                delay(10)
                                counterViewModel.toggle()
                                editVisibilityState.value = true
                            }
                        }
                    ) {
                        Icon(imageVector = icon, contentDescription = "Start/Stop")
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Picker(
    range: IntRange,
    indicator: String,
    textStyle: TextStyle = MaterialTheme.typography.h3.copy(fontSize = 30.sp),
    editState: MutableState<Boolean>,
    valueState: MutableState<Int>
) {
    Column(
        modifier = Modifier.width(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StepButton(range = range, valueState = valueState, editState = editState)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (editState.value) String.format(
                "%02d$indicator",
                valueState.value
            ) else String.format("%02d", valueState.value),
            style = if (editState.value) MaterialTheme.typography.h5 else textStyle,
            color = if (editState.value || indicator != "s") MaterialTheme.colors.onSurface else progressGradient[colorIndex.value]
        )
        Spacer(modifier = Modifier.height(8.dp))
        StepButton(range = range, multiplier = -1, valueState = valueState, editState = editState)
    }
}

@ExperimentalAnimationApi
@Composable
fun StepButton(
    range: IntRange,
    multiplier: Int = 1,
    valueState: MutableState<Int>,
    editState: MutableState<Boolean>
) {
    AnimatedVisibility(visible = editState.value) {
        IconButton(
            onClick = {
                val newValue = valueState.value + 1 * multiplier
                if (newValue in range) {
                    valueState.value = newValue
                }
            }
        ) {
            Icon(
                imageVector = if (multiplier > 0) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = ""
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun HourPicker(counterViewModel: CounterViewModel) {
    Picker(
        range = 0..23,
        editState = counterViewModel.editState,
        indicator = "h",
        valueState = counterViewModel.hourProgressState
    )
}

@ExperimentalAnimationApi
@Composable
fun MinutePicker(counterViewModel: CounterViewModel) {
    Picker(
        range = 0..59,
        editState = counterViewModel.editState,
        indicator = "m",
        valueState = counterViewModel.minuteProgressState
    )
}

@ExperimentalAnimationApi
@Composable
fun SecondPicker(counterViewModel: CounterViewModel) {
    Picker(
        range = 0..59,
        editState = counterViewModel.editState,
        indicator = "s",
        textStyle = MaterialTheme.typography.body2.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        ),
        valueState = counterViewModel.secondProgressState
    )
}

@Composable
fun AnimatedCircle(
    modifier: Modifier = Modifier,
    editState: MutableState<Boolean>,
    sweepAngleState: MutableState<Float>,
    lifecycleScope: LifecycleCoroutineScope
) {
    val stroke = with(LocalDensity.current) {
        Stroke(
            22.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(5F, 6F, 7F)
            )
        )
    }
    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        val startAngle = -90F
        drawArc(
            color = if (editState.value) Color.LightGray.copy(alpha = 0.5F) else purple700,
            startAngle = startAngle,
            sweepAngle = sweepAngleState.value,
            topLeft = topLeft,
            size = size,
            useCenter = false,
            style = stroke
        )
    }
    lifecycleScope.launchWhenStarted {
        while (isActive && !editState.value) {
            delay(300)
            var nextIndex = colorIndex.value + 1
            if (nextIndex > progressGradient.lastIndex)
                nextIndex = 0
            colorIndex.value = nextIndex
        }
    }
}
