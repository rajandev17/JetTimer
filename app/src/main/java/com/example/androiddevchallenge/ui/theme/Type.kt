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
package com.example.androiddevchallenge.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R

private val plasmaFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.ps_regular,
            weight = FontWeight.W400,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.ps_semi_bold,
            weight = FontWeight.W500,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.ps_bold,
            weight = FontWeight.W600,
            style = FontStyle.Normal
        )
    )
)

val titleFontFamily = FontFamily(listOf(Font(resId = R.font.title)))
val titleTextStyle = TextStyle(fontFamily = titleFontFamily, fontSize = 28.sp, color = purple500)

// Set of Material typography styles to start with
val typography = Typography(
    h2 = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 30.sp
    ),
    h3 = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 28.sp
    ),
    h4 = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    h5 = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    h6 = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = plasmaFontFamily,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    overline = TextStyle(
        fontFamily = plasmaFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    )
)
