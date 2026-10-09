package com.example.turbo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.ApexViewModel

data class UnifiedAppViewModels(
  val apexViewModel: ApexViewModel,
  val gameTurboViewModel: GameTurboViewModel
)
