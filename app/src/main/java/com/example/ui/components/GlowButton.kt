package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberTextPrimary

@Composable
fun TurboActivateButton(
  isActive: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "turboGlow")
  val glowAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 0.9f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "glowAlpha"
  )

  val borderColor = if (isActive) CyberNeonGreen else CyberNeonCyan
  val buttonText = if (isActive) "120 FPS TURBO ACTIVE" else "ACTIVATE 120 FPS TURBO"

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(58.dp)
      .shadow(elevation = if (isActive) 12.dp else 6.dp, shape = RoundedCornerShape(12.dp), ambientColor = borderColor)
      .clip(RoundedCornerShape(12.dp))
      .border(
        width = 1.5.dp,
        brush = Brush.horizontalGradient(
          listOf(
            CyberNeonCyan.copy(alpha = glowAlpha),
            CyberNeonGreen.copy(alpha = glowAlpha)
          )
        ),
        shape = RoundedCornerShape(12.dp)
      )
      .background(
        brush = Brush.horizontalGradient(
          colors = if (isActive) {
            listOf(
              Color(0xFF003822),
              Color(0xFF004D2E),
              Color(0xFF003822)
            )
          } else {
            listOf(
              Color(0xFF0B2138),
              Color(0xFF0A334C),
              Color(0xFF0B2138)
            )
          }
        )
      )
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      )
      .testTag("activate_turbo_button"),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.padding(horizontal = 16.dp)
    ) {
      Icon(
        imageVector = if (isActive) Icons.Default.CheckCircle else Icons.Default.Bolt,
        contentDescription = "Turbo Icon",
        tint = if (isActive) CyberNeonGreen else CyberNeonCyan,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = buttonText,
        color = CyberTextPrimary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 0.8.sp
      )
      if (isActive) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.Default.Speed,
          contentDescription = "Active Speed",
          tint = CyberNeonGreen,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
