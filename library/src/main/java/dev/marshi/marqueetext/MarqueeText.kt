package dev.marshi.marqueetext

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit

/**
 * @param durationMs 表示幅分を移動するのにかける時間(ms)
 * @param delayMs アニメーション開始位置で静止する時間(ms)
 * @param spaceRatio (空白/表示幅)の割合
 */
@Composable
fun MarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    durationMs: Int = 7500,
    delayMs: Int = 3000,
    spaceRatio: Float = 0.3f,
) {
    val createText = @Composable { localModifier: Modifier ->
        Text(
            text,
            textAlign = textAlign,
            modifier = localModifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = 1,
            onTextLayout = onTextLayout,
            style = style,
        )
    }
    val textLayoutInfoState = remember { mutableStateOf<TextLayoutInfo?>(null) }
    val textLayoutInfo = textLayoutInfoState.value
    val transition = rememberInfiniteTransition()
    val initialValue = 0
    val offset by if (textLayoutInfo != null) {
        val duration = durationMs * textLayoutInfo.textWidth / textLayoutInfo.containerWidth
        transition.animateValue(
            initialValue = initialValue,
            targetValue = -textLayoutInfo.textWidth,
            typeConverter = Int.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = duration,
                    delayMillis = delayMs,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart
            ),
        )
    } else {
        remember { mutableStateOf(initialValue) }
    }

    SubcomposeLayout(
        modifier = modifier.clipToBounds()
    ) { constraints ->
        val infiniteWidthConstraints = constraints.copy(maxWidth = Constraints.Infinity)
        var mainText = subcompose(MarqueeLayers.MainText) {
            createText(textModifier)
        }.first().measure(infiniteWidthConstraints)

        var trailingPlaceableWithOffset: Pair<Placeable, Int>? = null
        if (mainText.width <= constraints.maxWidth) {
            mainText = subcompose(MarqueeLayers.TrailingText) {
                createText(textModifier)
            }.first().measure(constraints)
            textLayoutInfoState.value = null
        } else {
            val spacing = (constraints.maxWidth * spaceRatio).toInt()
            textLayoutInfoState.value = TextLayoutInfo(
                textWidth = mainText.width + spacing, // テキストの長さ + widthの2/3の長さ
                containerWidth = constraints.maxWidth // 画面幅
            )
            // offsetは `0 ~ -(mainText.width + spacing)`の間を変化する.
            // したがってtrailingTextOffsetの値は `0 ~ (mainText.width + spacing)` の間を変化する.
            // これはmarquee効果で流れてくるテキストの左端の位置を表す.
            val trailingTextOffset = mainText.width + spacing + offset
            val trailingTextSpace = constraints.maxWidth - trailingTextOffset
            if (trailingTextSpace > 0) {
                // marquee効果で流れてくるテキストの左端の位置が描画範囲内にある場合
                trailingPlaceableWithOffset = subcompose(MarqueeLayers.TrailingText) {
                    createText(textModifier)
                }.first().measure(constraints) to trailingTextOffset
            }
        }

        layout(
            width = constraints.maxWidth,
            height = mainText.height
        ) {
            mainText.place(offset, 0)
            trailingPlaceableWithOffset?.let { (placable, trailingTextOffset) ->
                placable.place(trailingTextOffset, 0)
            }
        }
    }
}

@Preview(widthDp = 100)
@Composable
fun PreviewMarqueeText() {
    Column {
        MarqueeText(text = "aiueo")
        MarqueeText(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
    }
}

private enum class MarqueeLayers { MainText, TrailingText }
private data class TextLayoutInfo(
    val textWidth: Int,
    val containerWidth: Int
)
