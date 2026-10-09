package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.data.models.CrosshairStyle
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardSecondary

@Composable
fun CustomCrosshairCanvas(
  style: CrosshairStyle,
  colorHex: String,
  scaleDp: Float,
  opacity: Float,
  isRecoil: Boolean,
  modifier: Modifier = Modifier
) {
  val animatedRecoilExpansion by animateFloatAsState(
    targetValue = if (isRecoil) 18f else 0f,
    animationSpec = spring(dampingRatio = 0.4f, stiffness = 600f),
    label = "crosshairRecoil"
  )

  val parsedColor = try {
    Color(android.graphics.Color.parseColor(colorHex)).copy(alpha = opacity)
  } catch (_: Exception) {
    Color(0xFF00FF66).copy(alpha = opacity)
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(CyberCardSecondary)
      .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp)),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val center = Offset(size.width / 2f, size.height / 2f)

      // Background sniper HUD grid
      drawCircle(
        color = Color(0xFF1E293B).copy(alpha = 0.6f),
        radius = size.width * 0.38f,
        center = center,
        style = Stroke(width = 1.dp.toPx())
      )
      drawCircle(
        color = Color(0xFF1E293B).copy(alpha = 0.4f),
        radius = size.width * 0.22f,
        center = center,
        style = Stroke(width = 0.8.dp.toPx())
      )
      // Grid cross lines
      drawLine(
        color = Color(0xFF1E293B).copy(alpha = 0.3f),
        start = Offset(center.x, center.y - size.height * 0.4f),
        end = Offset(center.x, center.y + size.height * 0.4f),
        strokeWidth = 1.dp.toPx()
      )
      drawLine(
        color = Color(0xFF1E293B).copy(alpha = 0.3f),
        start = Offset(center.x - size.width * 0.4f, center.y),
        end = Offset(center.x + size.width * 0.4f, center.y),
        strokeWidth = 1.dp.toPx()
      )

      // Dynamic crosshair rendering
      val gap = (scaleDp * 0.25f + animatedRecoilExpansion).dp.toPx()
      val armLength = (scaleDp * 0.6f).dp.toPx()
      val strokeW = (2.2f).dp.toPx()

      when (style) {
        CrosshairStyle.CROSS -> {
          // Top, bottom, left, right arms
          drawLine(
            color = parsedColor,
            start = Offset(center.x, center.y - gap),
            end = Offset(center.x, center.y - gap - armLength),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
          )
          drawLine(
            color = parsedColor,
            start = Offset(center.x, center.y + gap),
            end = Offset(center.x, center.y + gap + armLength),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
          )
          drawLine(
            color = parsedColor,
            start = Offset(center.x - gap, center.y),
            end = Offset(center.x - gap - armLength, center.y),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
          )
          drawLine(
            color = parsedColor,
            start = Offset(center.x + gap, center.y),
            end = Offset(center.x + gap + armLength, center.y),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
          )
          // Center dot
          drawCircle(
            color = parsedColor,
            radius = (1.5f).dp.toPx(),
            center = center
          )
        }
        CrosshairStyle.CIRCLE -> {
          val circleRadius = (scaleDp * 0.5f + animatedRecoilExpansion * 0.5f).dp.toPx()
          drawCircle(
            color = parsedColor,
            radius = circleRadius,
            center = center,
            style = Stroke(width = strokeW)
          )
          drawCircle(
            color = parsedColor,
            radius = (1.8f).dp.toPx(),
            center = center
          )
        }
        CrosshairStyle.DOT -> {
          val dotRadius = (scaleDp * 0.25f + animatedRecoilExpansion * 0.2f).dp.toPx()
          drawCircle(
            color = parsedColor,
            radius = dotRadius,
            center = center
          )
        }
        CrosshairStyle.T_SHAPE -> {
          // Bottom, left, right (No top arm)
          drawLine(
            color = parsedColor,
            start = Offset(center.x, center.y + gap),
            end = Offset(center.x, center.y + gap + armLength),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
          )
          drawLine(
            color = parsedColor,
            start = Offset(center.x - gap, center.y),
            end = Offset(center.x - gap - armLength, center.y),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
          )
          drawLine(
            color = parsedColor,
            start = Offset(center.x + gap, center.y),
            end = Offset(center.x + gap + armLength, center.y),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
          )
          drawCircle(
            color = parsedColor,
            radius = (1.5f).dp.toPx(),
            center = center
          )
        }
        CrosshairStyle.CHEVRON -> {
          val chevH = (scaleDp * 0.5f).dp.toPx()
          val chevW = (scaleDp * 0.4f).dp.toPx()
          val path = Path().apply {
            moveTo(center.x - chevW, center.y + chevH * 0.5f + animatedRecoilExpansion)
            lineTo(center.x, center.y - chevH * 0.5f)
            lineTo(center.x + chevW, center.y + chevH * 0.5f + animatedRecoilExpansion)
          }
          drawPath(
            path = path,
            color = parsedColor,
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
          )
          drawCircle(
            color = parsedColor,
            radius = (1.5f).dp.toPx(),
            center = center
          )
        }
      }
    }
  }
}
