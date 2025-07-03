package com.eattalk.table.model

import android.icu.util.ULocale

// 추후 변경될 여지가 있음.
data class Language(val code: ULocale, val displayName: String, val isDefault: Boolean = false)


