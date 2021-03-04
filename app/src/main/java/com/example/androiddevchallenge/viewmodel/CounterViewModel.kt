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
package com.example.androiddevchallenge.viewmodel

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.animation.LinearInterpolator
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {

    companion object {
        private const val HOUR_MULTIPLIER = 3600
        private const val MINUTE_MULTIPLIER = 60
    }

    val editState = mutableStateOf(true)
    val sweepAngleState = mutableStateOf(360F)
    val hourProgressState = mutableStateOf(0)
    val minuteProgressState = mutableStateOf(0)
    val secondProgressState = mutableStateOf(0)
    var animator: ValueAnimator? = null

    @SuppressLint("Recycle")
    private fun startTimer() = viewModelScope.launch {
        var timeInSeconds = secondProgressState.value + (minuteProgressState.value * MINUTE_MULTIPLIER) + (hourProgressState.value * HOUR_MULTIPLIER)
        if (timeInSeconds <= 0) return@launch

        animator = ValueAnimator.ofFloat(360F, 0F)
        animator?.duration = timeInSeconds * 1000L
        animator?.interpolator = LinearInterpolator()
        animator?.addUpdateListener {
            sweepAngleState.value = it.animatedValue as Float
        }
        listenToAnimation()
        animator?.start()
        editState.value = false
        while (timeInSeconds > 0 && !editState.value) {
            updateTime(timeInSeconds--)
            delay(1000)
        }
        updateTime(0)
        editState.value = true
        cancel()
    }

    private fun listenToAnimation() {
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                sweepAngleState.value = 360F
            }

            override fun onAnimationEnd(animation: Animator?) {
                sweepAngleState.value = 360F
            }

            override fun onAnimationCancel(animation: Animator?) {
                sweepAngleState.value = 360F
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

    private fun updateTime(seconds: Int) {
        var timeInSeconds = seconds
        val hours = timeInSeconds / HOUR_MULTIPLIER
        timeInSeconds %= HOUR_MULTIPLIER
        val minutes = timeInSeconds / MINUTE_MULTIPLIER
        timeInSeconds %= MINUTE_MULTIPLIER

        hourProgressState.value = hours
        minuteProgressState.value = minutes
        secondProgressState.value = timeInSeconds
    }

    private fun cancelTimer() {
        animator?.cancel()
        sweepAngleState.value = 360F
        editState.value = true
    }

    fun toggle() {
        if (editState.value) {
            startTimer()
        } else {
            cancelTimer()
        }
    }
}
