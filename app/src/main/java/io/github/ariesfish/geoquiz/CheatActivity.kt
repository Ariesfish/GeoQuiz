package io.github.ariesfish.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cheat.*

class CheatActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ANSWER = "io.github.ariesfish.geoquiz.answer"
        private const val EXTRA_CHEAT_COUNT = "io.github.ariesfish.geoquiz.cheat_count"
        private const val EXTRA_ANSWER_SHOWN = "io.github.ariesfish.geoquiz.answer_shown"
        private const val ANSWER_SHOWN_KEY = "answer_shown"

        fun newIntent(context: Context, answer: Boolean, cheatCount: Int): Intent {
            val cheatIntent = Intent(context, CheatActivity::class.java)
            cheatIntent.putExtra(EXTRA_ANSWER, answer)
            cheatIntent.putExtra(EXTRA_CHEAT_COUNT, cheatCount)
            return cheatIntent
        }

        fun wasAnswerShown(result: Intent?): Boolean =
            result?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
    }

    private var isAnswerShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        isAnswerShown = savedInstanceState?.getBoolean(ANSWER_SHOWN_KEY) ?: false
        val answer = intent.getBooleanExtra(EXTRA_ANSWER, false)
        val cheatCount = intent.getIntExtra(EXTRA_CHEAT_COUNT, 3)

        if (isAnswerShown) showAnswer(answer)

        cheatCountTextView.text = "Left cheat count is $cheatCount"

        showAnswerButton.setOnClickListener {
            showAnswer(answer)
            isAnswerShown = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(ANSWER_SHOWN_KEY, isAnswerShown)
    }

    override fun onBackPressed() {
        val data = Intent().putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(Activity.RESULT_OK, data)
        super.onBackPressed()
    }

    private fun showAnswer(answer: Boolean) {
        answerTextView.setText(
            if (answer) R.string.true_button
            else R.string.false_button
        )
    }
}
