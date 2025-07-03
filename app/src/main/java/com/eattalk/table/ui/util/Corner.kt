package com.eattalk.table.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.pow

// 개선된 iOS 스타일 Squircle Shape 구현
class ImprovedSquircleShape(private val smoothFactor: Float = 0.6f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height

            // iOS squircle에 가까운 형태를 위한 파라미터
            // smoothFactor 값이 클수록 더 둥글게 됨 (0.0 = 사각형, 1.0 = 원형)
            val n = 5 - (smoothFactor * 3) // 일반적으로 iOS는 n=4~5 사이 값을 사용

            addSquircle(
                rect = Rect(0f, 0f, width, height),
                cornerRadius = minOf(width, height) / 4, // 코너 반경
                smoothness = n.toFloat()
            )
        }

        return Outline.Generic(path)
    }

    // 슈퍼 타원을 그리는 개선된 함수
    private fun Path.addSquircle(rect: Rect, cornerRadius: Float, smoothness: Float) {
        val width = rect.width
        val height = rect.height

        // 더 많은 포인트로 더 부드러운 곡선 생성
        val steps = 120

        reset()
        for (i in 0 until steps) {
            val theta = i * 2.0f * Math.PI.toFloat() / steps

            // 슈퍼 타원 좌표 계산 (Lamé curve)
            val cosTheta = Math.cos(theta.toDouble()).toFloat()
            val sinTheta = Math.sin(theta.toDouble()).toFloat()

            // |cos(t)|^n * a * sgn(cos(t)) 형태의 슈퍼 타원 공식
            val x = width / 2 + (width / 2) * Math.abs(cosTheta).pow(2.0f / smoothness) * Math.signum(cosTheta.toDouble()).toFloat()
            val y = height / 2 + (height / 2) * Math.abs(sinTheta).pow(2.0f / smoothness) * Math.signum(sinTheta.toDouble()).toFloat()

            if (i == 0) {
                moveTo(x, y)
            } else {
                lineTo(x, y)
            }
        }
        close()
    }
}

// 간소화된, iOS 스타일 코너를 위한 간단한 대안 구현
class SimpleSquircleShape(private val cornerRadius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            Path().apply {
                val radius = size.minDimension * cornerRadius

                // 상단 왼쪽에서 시작
                moveTo(radius, 0f)

                // 상단 오른쪽으로
                lineTo(size.width - radius, 0f)
                // 곡선으로 오른쪽 상단 코너
                cubicTo(
                    size.width - radius / 2, 0f,
                    size.width, radius / 2,
                    size.width, radius
                )

                // 오른쪽 하단으로
                lineTo(size.width, size.height - radius)
                // 곡선으로 오른쪽 하단 코너
                cubicTo(
                    size.width, size.height - radius / 2,
                    size.width - radius / 2, size.height,
                    size.width - radius, size.height
                )

                // 하단 왼쪽으로
                lineTo(radius, size.height)
                // 곡선으로 왼쪽 하단 코너
                cubicTo(
                    radius / 2, size.height,
                    0f, size.height - radius / 2,
                    0f, size.height - radius
                )

                // 왼쪽 상단으로
                lineTo(0f, radius)
                // 곡선으로 왼쪽 상단 코너
                cubicTo(
                    0f, radius / 2,
                    radius / 2, 0f,
                    radius, 0f
                )

                close()
            }
        )
    }
}

// 2. 연속적인 코너(Continuous Corner) 사용 - 더 매끄러운 전환
class ContinuousCornerShape(private val radius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            // 둥근 사각형을 만들되 베지어 곡선으로 코너 부분을 보간
            val width = size.width
            val height = size.height
            val r = radius * minOf(width, height)

            // 상단 왼쪽에서 시작
            moveTo(r, 0f)

            // 상단 오른쪽으로
            lineTo(width - r, 0f)
            quadraticTo(width, 0f, width, r)

            // 오른쪽 하단으로
            lineTo(width, height - r)
            quadraticTo(width, height, width - r, height)

            // 하단 왼쪽으로
            lineTo(r, height)
            quadraticTo(0f, height, 0f, height - r)

            // 왼쪽 상단으로
            lineTo(0f, r)
            quadraticTo(0f, 0f, r, 0f)

            close()
        }

        return Outline.Generic(path)
    }
}

// 3. 더 부드러운 RoundedCornerShape (3차 베지어 곡선 사용)
class SmoothRoundedCornerShape(private val radius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            val r = radius * minOf(width, height)

            // 매직 넘버 0.552284749831 - 원을 만드는 최적의 베지어 곡선 비율
            val handleSize = r * 0.552284749831f

            // 상단 왼쪽에서 시작
            moveTo(r, 0f)

            // 상단 오른쪽으로
            lineTo(width - r, 0f)
            cubicTo(width - r + handleSize, 0f, width, r - handleSize, width, r)

            // 오른쪽 하단으로
            lineTo(width, height - r)
            cubicTo(width, height - r + handleSize, width - r + handleSize, height, width - r, height)

            // 하단 왼쪽으로
            lineTo(r, height)
            cubicTo(r - handleSize, height, 0f, height - r + handleSize, 0f, height - r)

            // 왼쪽 상단으로
            lineTo(0f, r)
            cubicTo(0f, r - handleSize, r - handleSize, 0f, r, 0f)

            close()
        }

        return Outline.Generic(path)
    }
}

// Switch에 사용할 수 있는 매끄러운 형태의 트랙 Shape
class SmoothSwitchTrackShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadius = size.height / 2
        return Outline.Rounded(
            RoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                cornerRadius = CornerRadius(cornerRadius)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CornerShapesPreview() {
    Surface {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 기본 RoundedCornerShape
            Text("기본 RoundedCornerShape")
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1A73E8))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(ImprovedSquircleShape(0.6f))
                    .background(Color(0xFF1A73E8))
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(ImprovedSquircleShape(0.8f))
                    .background(Color(0xFF1A73E8))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Continuous Corner Shape
            Text("Continuous Corner Shape")
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(ContinuousCornerShape(0.2f))
                    .background(Color(0xFF1A73E8))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Smooth Rounded Corner Shape
            Text("Smooth Rounded Corner Shape")
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(SmoothRoundedCornerShape(0.2f))
                    .background(Color(0xFF1A73E8))
            )
        }
    }
}