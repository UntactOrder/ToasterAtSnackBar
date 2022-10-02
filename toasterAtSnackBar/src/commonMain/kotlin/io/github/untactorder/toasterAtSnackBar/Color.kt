/*
 * Copyright 2022 The UntactOrder Project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


package io.github.untactorder.toasterAtSnackBar

import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

data class ToastColorSet(
    val ordinal: Color, val plain: Color, val info: Color, val retry: Color,
    val success: Color, val warning: Color, val error: Color
)

fun ToastColorSet.toList(): List<Color> {
    return listOf(this.ordinal, this.plain, this.info, this.retry, this.success, this.warning, this.error)
}

// FancyColorSet
val FancyOrdinal = Color(0xFFF4F5F7)
val FancyPlain = Color(0xFF3D3A3A)
val FancyInfo = Color(0xFF607D8B)
val FancyRetry = Color(0xFF1B8DB5)
val FancySuccess = Color(0xFF0F9960)
val FancyWarning = Color(0xFFF69200)
val FancyError = Color(0xFFF44336)
val FancyColorSet = ToastColorSet(
    FancyOrdinal, FancyPlain, FancyInfo, FancyRetry, FancySuccess, FancyWarning, FancyError
)

// FancyBrightColorSet
val FancyBrightOrdinal = White
val FancyBrightPlain = Color(0xFFEFEBE7)
val FancyBrightInfo = Color(0xFFD5DDE1)
val FancyBrightRetry = Color(0xFFD2DDEA)
val FancyBrightSuccess = Color(0xFFC9E8BE)
val FancyBrightWarning = Color(0xFFF4D8B6)
val FancyBrightError = Color(0xFFF6CCC2)
val FancyBrightColorSet = ToastColorSet(
    FancyBrightOrdinal, FancyBrightPlain, FancyBrightInfo, FancyBrightRetry,
    FancyBrightSuccess, FancyBrightWarning, FancyBrightError
)

// FancyDarkColorSet
val FancyDarkOrdinal = Color(0xFF6E6E6E)
val FancyDarkPlain = Color(0xFF414141)
val FancyDarkInfo = Color(0xFF556E7B)
val FancyDarkRetry = Color(0xFF295382)
val FancyDarkSuccess = Color(0xFF1A621E)
val FancyDarkWarning = Color(0xFFBA8346)
val FancyDarkError = Color(0xFFCD382D)
val FancyDarkColorSet = ToastColorSet(
    FancyDarkOrdinal, FancyDarkPlain, FancyDarkInfo, FancyDarkRetry,
    FancyDarkSuccess, FancyDarkWarning, FancyDarkError
)

// PastelColorSet
val PastelOrdinal = Color(0xFFA85C70)
val PastelPlain = Color(0xFF8782B4)
val PastelInfo = Color(0xFF7C6FCA)
val PastelRetry = Color(0xFF7C6FCA)
val PastelSuccess = Color(0xFF3BBD9F)
val PastelWarning = Color(0xFFFFA537)
val PastelError = Color(0xFFFF515A)
val PastelColorSet = ToastColorSet(
    PastelOrdinal, PastelPlain, PastelInfo, PastelRetry, PastelSuccess, PastelWarning, PastelError
)

// ImpressiveColorSet
val ImpressiveOrdinal = Color(0xFF0062C4)
val ImpressivePlain = Color(0xFF307DB2)
val ImpressiveInfo = Color(0xFF307DB2)
val ImpressiveRetry = Color(0xFF109455)
val ImpressiveSuccess = Color(0xFFEE8526)
val ImpressiveWarning = Color(0xFFF63C2E)
val ImpressiveError = Color(0xFFC82C41)
val ImpressiveColorSet = ToastColorSet(
    ImpressiveOrdinal, ImpressivePlain, ImpressiveInfo, ImpressiveRetry, ImpressiveSuccess, ImpressiveWarning, ImpressiveError
)

// ImpressiveDarkColorSet
val ImpressiveDarkOrdinal = Color(0xFF043B72)
val ImpressiveDarkPlain = Color(0xFF136095)
val ImpressiveDarkInfo = Color(0xFF376357)
val ImpressiveDarkRetry = Color(0xFF005033)
val ImpressiveDarkSuccess = Color(0xFF9D4417)
val ImpressiveDarkWarning = Color(0xFFAD170F)
val ImpressiveDarkError = Color(0xFF7C1233)
val ImpressiveDarkColorSet = ToastColorSet(
    ImpressiveDarkOrdinal, ImpressiveDarkPlain, ImpressiveDarkInfo, ImpressiveDarkRetry,
    ImpressiveDarkSuccess, ImpressiveDarkWarning, ImpressiveDarkError
)
