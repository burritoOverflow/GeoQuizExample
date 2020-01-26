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

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )


    var currentIndex = 0
    var correctGuesses = 0

    // return the answer and text for the current question
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId


    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = currentIndex - 1
        if (currentIndex < 0) {
            val lastIndex = questionBank.size - 1
            currentIndex = lastIndex
            Log.d(
                TAG, "Current question index should be last in questionBankSize: " +
                        "Current: $currentIndex QuestionBank Size: $lastIndex"
            )
        }
    }

    fun incrementCorrectCounter() {
        correctGuesses++
    }
}