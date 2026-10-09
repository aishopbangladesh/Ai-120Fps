package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardSecondary
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularGaugeDial(
  valueText: String,
  labelText: String,
  progress: Float = 0.85f,
  isLocked: Boolean = true,
  glowColor: Color = CyberNeonGreen,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "gaugePulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Box(
    modifier = modifier
      .size(130.dp)
      .clip(CircleShape)
      .background(CyberCardSecondary.copy(alpha = 0.6f))
      .border(1.dp, CyberCardBorder, CircleShape),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(116.dp)) {
      val strokeWidth = 8.dp.toPx()
      val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
      val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

      // Background track
      drawArc(
        color = Color(0xFF1E293B),
        startAngle = 135f,
        sweepAngle = 270f,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
      )

      // Active Neon Arc with Gradient
      drawArc(
        brush = Brush.sweepGradient(
          colors = listOf(CyberNeonCyan, glowColor.copy(alpha = pulseAlpha), CyberNeonGreen)
        ),
        startAngle = 135f,
        sweepAngle = 270f * progress,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
      )

      // Subtle tick indicators
      val center = Offset(size.width / 2f, size.height / 2f)
      val tickRadius = (size.width / 2f) - strokeWidth - 4.dp.toPx()
      for (i in 0..8) {
        val angleDeg = 135f + (270f / 8f) * i
        val angleRad = (angleDeg * PI / 180f).toFloat()
        val tickStart = Offset(
          center.x + tickRadius * cos(angleRad),
          center.y + tickRadius * sin(angleRad)
        )
        val tickEnd = Offset(
          center.x + (tickRadius - 4.dp.toPx()) * cos(angleRad),
          center.y + (tickRadius - 4.dp.toPx()) * sin(angleRad)
        )
        drawCircle(
          color = glowColor.copy(alpha = 0.35f),
          radius = 1.2.dp.toPx(),
          center = tickStart
        )
      }
    }

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = valueText,
        color = CyberTextPrimary,
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        fontFamily = FontFamily.SansSerif,
        letterSpacing = (-0.5).sp
      )
      Spacer(modifier = Modifier.height(2.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(4.dp))
          .background(glowColor.copy(alpha = 0.15f))
          .border(0.5.dp, glowColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = labelText,
          color = glowColor,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun RamStatusBar(
  freeRamGb: Double,
  totalRamGb: Double,
  modifier: Modifier = Modifier
) {
  val usedRamGb = totalRamGb - freeRamGb
  val ramFraction = (usedRamGb / totalRamGb).toFloat().coerceIn(0f, 1f)

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(CyberCard)
      .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
      .padding(horizontal = 12.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Memory,
          contentDescription = "RAM",
          tint = CyberNeonCyan,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = "RAM",
          color = CyberTextSecondary,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }

      // Progress bar in middle
      Box(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 10.dp)
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp))
          .background(Color(0xFF1E293B))
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(ramFraction)
            .height(6.dp)
            .background(
              Brush.horizontalGradient(
                listOf(CyberNeonCyan, CyberNeonGreen)
              )
            )
        )
      }

      Text(
        text = "${String.format("%.1f", freeRamGb)} GB / ${totalRamGb.toInt()} GB Free",
        color = CyberNeonGreen,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}
