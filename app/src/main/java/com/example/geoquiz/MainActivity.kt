package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CHEAT_CODE = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var correctGuessTextView: TextView

    // lazy initialization wont occur until the first time
    // this view model is accessed
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        trueButton.isEnabled = true
        falseButton.isEnabled = true
    }


    private fun incrementCorrectCounter() {
        quizViewModel.incrementCorrectCounter()
    }

    private fun displayCorrectCounter() {
        correctGuessTextView.text = quizViewModel.correctGuesses.toString()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        trueButton.isEnabled = false
        falseButton.isEnabled = false

        val correctAnswer = quizViewModel.currentQuestionAnswer


        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        // if correct, update the counter and display the new value
        if (userAnswer == correctAnswer) {
            incrementCorrectCounter()
            displayCorrectCounter()
        }

        val toast = Toast.makeText(this, messageResId, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 100)
        toast.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the initial state
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        previousButton = findViewById(R.id.previous_button)
        questionTextView = findViewById(R.id.question_text_view)
        correctGuessTextView = findViewById(R.id.correct_counter)

        displayCorrectCounter()

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        cheatButton.setOnClickListener {
            // start cheat activity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CHEAT_CODE)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        previousButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }


        // get the question text to be displayed in the view by
        // the current index
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CHEAT_CODE) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_IS_SHOWN, false) ?: false
        }
    }


    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "Save instance state")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

}
