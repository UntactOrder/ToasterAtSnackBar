package io.github.untactorder.toasterAtSnackBar.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


// Fixed Colors
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

val GrayPrimaryLight = Color(0xFFF1F2F4)
val GrayPrimaryDark = Color(0xFF3B3B42)
val GreyDeep = Color(0xFF707070)
val GreyDeepDark = Color(0xFF3C3B41)
val Magenta = Color(0xFFFF7C80)
val Yellow = Color(0xFFF9BF00)
val PurpleDeep = Color(0xFF9291BE)


// UntactOrder Project Signature Colors (Light Mode)
val SignatureRed = Color(0xFFFF667A)
val SignatureYellow = Color(0xFFF6D56A)
val SignatureGreen = Color(0xFF2CC898)
val SignatureBlue = Color(0xFF4285F0)
val SignaturePurple = Color(0xFF9063E9)
val SignatureWhite = White
val SignatureGrey = Color(0xFFEAEAEA)
val SignatureText = Black
val SignatureRipple = Color(0xFF949090)
val SignatureBackBoard = White
val SignatureBackground = Color(0xFFF7F7F7)


// UntactOrder Project Signature Colors (Dark Mode)
val DKSignatureRed = Color(0xFFDD5168)
val DKSignatureYellow = Color(0xFFEBAC0F)
val DKSignatureGreen = Color(0xFF27AF85)
val DKSignatureBlue = Color(0xFF2984C9)
val DKSignaturePurple = Color(0xFF8453E7)
val DKSignatureWhite = Color(0xFFD0CECE)
val DKSignatureGrey = Color(0xFF646060)
val DKSignatureText = White
val DKSignatureRipple = Color(0xFFDADADA)
val DKSignatureBackBoard = Black
val DKSignatureBackground = Color(0xFF353333)


// UntactOrder Project Signature Color Data Class
data class SignatureColors(
    val red: Color, val yellow: Color, val green: Color, val blue: Color,
    val purple: Color, val white: Color, val grey: Color, val text: Color,
    val ripple: Color, val backBoard: Color, val background: Color)

val SignatureColorsLight = SignatureColors(
    SignatureRed, SignatureYellow, SignatureGreen, SignatureBlue,
    SignaturePurple, SignatureWhite, SignatureGrey, SignatureText,
    SignatureRipple, SignatureBackBoard, SignatureBackground
)

val SignatureColorsDark = SignatureColors(
    DKSignatureRed, DKSignatureYellow, DKSignatureGreen, DKSignatureBlue,
    DKSignaturePurple, DKSignatureWhite, DKSignatureGrey, DKSignatureText,
    DKSignatureRipple, DKSignatureBackBoard, DKSignatureBackground
)


// UntactOrder Project Item List Colors
val ItemListColors = arrayOf(
    Color(0xFFE14B60), Color(0xFFEB734D), Color(0xFFEF933D),
    Color(0xFFF4CB46), Color(0xFFA9DB1B), Color(0xFF55B96D),
    Color(0xFF7D918C), Color(0xFF978D70))
