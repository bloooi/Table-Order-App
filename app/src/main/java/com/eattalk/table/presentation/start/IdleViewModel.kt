
package com.eattalk.table.presentation.start

import androidx.lifecycle.ViewModel
import com.eattalk.table.model.Language
import com.eattalk.table.ui.util.DialogManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IdleViewModel @Inject constructor() : ViewModel() {

    val dialogManager = DialogManager()

