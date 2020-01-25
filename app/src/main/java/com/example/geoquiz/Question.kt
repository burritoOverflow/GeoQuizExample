package com.example.geoquiz

import androidx.annotation.StringRes

// textResId is Int as the variable stores the resource ID
data class Question(@StringRes val textResId: Int, val answer: Boolean)