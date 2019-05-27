package io.github.ariesfish.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "QuizActivity"
        private const val INDEX_KEY = "index"
        private const val QUESTIONS_KEY = "questions"
        private const val CHEAT_COUNT_KEY = "cheat count"
        private const val CHEAT_REQUEST = 0
    }

    private var questionIndex = 0
    private var cheatCount = 3
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionIndex = savedInstanceState?.getInt(INDEX_KEY) ?: 0
        cheatCount = savedInstanceState?.getInt(CHEAT_COUNT_KEY) ?: 3

        updateQuestion()

        questionTextView.setOnClickListener {
            nextQuestion()
        }

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            nextQuestion()
        }

        prevButton.setOnClickListener {
            prevQuestion()
        }

        cheatButton.setOnClickListener {
            startActivityForResult(CheatActivity.newIntent(this,
                questionBank[questionIndex].answer, cheatCount),
                CHEAT_REQUEST)
        }
    }

    override fun onResume() {
        super.onResume()
        if (cheatCount <= 0) cheatButton.visibility = View.INVISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.i(LOG_TAG, "onSaveInstanceState")
        outState?.putInt(INDEX_KEY, questionIndex)
        outState?.putInt(CHEAT_COUNT_KEY, cheatCount)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHEAT_REQUEST && resultCode == Activity.RESULT_OK) {
            val shown = CheatActivity.wasAnswerShown(data)
            if (shown) cheatCount -= 1
            questionBank[questionIndex].isCheated = shown
        }
    }

    private fun nextQuestion() {
        questionIndex = (questionIndex + 1) % questionBank.size
        updateQuestion()
    }

    private fun prevQuestion() {
        questionIndex = (questionIndex - 1 + questionBank.size) % questionBank.size
        updateQuestion()
    }

    private fun updateQuestion() =
        questionTextView.setText(questionBank[questionIndex].questionResId)

    private fun checkAnswer(userPressed: Boolean) {
        val text =
            if (questionBank[questionIndex].isCheated) {
                R.string.judgment_toast
            }
            else {
                if (questionBank[questionIndex].answer == userPressed)
                    R.string.correct_toast
                else R.string.incorrect_toast
            }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        questionBank[questionIndex].isAnswered = true
    }
}
