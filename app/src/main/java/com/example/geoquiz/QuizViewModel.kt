package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    init {
        Log.d(TAG, "View model created.")
    }

    // called before the onDestroy method is called in mainActivity
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "OnCleared() called. View Model about to be destroyed.")
    }
}