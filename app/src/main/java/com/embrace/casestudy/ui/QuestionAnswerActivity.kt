package com.embrace.casestudy.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.embrace.casestudy.R
import com.embrace.casestudy.databinding.ActivityQuestionAnswersBinding
import com.embrace.casestudy.model.Question
import com.embrace.casestudy.model.QuestionAnswers
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class QuestionAnswerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var questionList: ArrayList<Question>
    private lateinit var binding: ActivityQuestionAnswersBinding
    private lateinit var counterDownTimer: CountDownTimer
    private var count = 0    //use for iteration the list of questions
    private var yScore = 0   //use for score of current user

    private var answerMultiple: ArrayList<String> = ArrayList()
    private var progressCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionAnswersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataFromPreviousActivity()

        binding.tvAnswerOne.setOnClickListener(this)
        binding.tvAnswerTwo.setOnClickListener(this)
        binding.tvAnswerThree.setOnClickListener(this)
        binding.tvAnswerFour.setOnClickListener(this)
        binding.tvAnswerFive.setOnClickListener(this)
    }

    private fun getDataFromPreviousActivity() {

        val bundle = intent.getBundleExtra("Bundle")
        var question: QuestionAnswers = bundle!!.getParcelable("questionList")!!
        questionList = question.questionList

        setAllViews()
    }

    private fun setAllViews() {
        settingViewVisibility()
        settingDataInViews()
        settingProgressBar()
    }

    private fun settingProgressBar() {

        counterDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressCount += 1
                binding.progressBar.progress = progressCount
                binding.progressStatus.text =
                    "$progressCount / ${binding.progressBar.max}"
            }

            override fun onFinish() {
                viewClickDisable()
                waitAndRestart()
            }
        }
        counterDownTimer.start()

    }

    private fun settingDataInViews() {

        binding.tvQuizNo.text = "${count + 1}"
        binding.tvYScore.text = "$yScore"


        binding.tvQuestion.text = questionList[count].question
        binding.tvQScore.text = "${questionList[count].score}"

        questionList[count].questionImageUrl.let {
            binding.ivLogo.visibility = View.VISIBLE
            Glide.with(binding.ivLogo).load(questionList[count].questionImageUrl)
                .into(binding.ivLogo)
        }


        questionList[count].answers?.A?.let {
            binding.tvAnswerOne.visibility = View.VISIBLE
            binding.tvAnswerOne.text = it
        }

        questionList[count].answers?.B?.let {
            binding.tvAnswerTwo.visibility = View.VISIBLE
            binding.tvAnswerTwo.text = it
        }

        questionList[count].answers?.C?.let {
            binding.tvAnswerThree.visibility = View.VISIBLE
            binding.tvAnswerThree.text = it
        }

        questionList[count].answers?.D?.let {
            binding.tvAnswerFour.visibility = View.VISIBLE
            binding.tvAnswerFour.text = it
        }

        questionList[count].answers?.E?.let {
            binding.tvAnswerFive.visibility = View.VISIBLE
            binding.tvAnswerFive.text = it
        }

    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.tvAnswerOne -> {
                checkingTheAnswer("A")
            }
            R.id.tvAnswerTwo -> {
                checkingTheAnswer("B")
            }
            R.id.tvAnswerThree -> {
                checkingTheAnswer("C")
            }
            R.id.tvAnswerFour -> {
                checkingTheAnswer("D")
            }
            R.id.tvAnswerFive -> {
                checkingTheAnswer("E")
            }
        }
    }

    private fun checkingTheAnswer(answer: String) {
        var correctAnswer = questionList[count].correctAnswer

        if (correctAnswer?.contains(answer) == true) {
            yScore += questionList[count].score!!
            changeBackground(answer, "green")
        } else {
            changeBackground(correctAnswer!!, "green")
            changeBackground(answer, "red")
        }
    }


    private fun changeBackground(answer: String, color: String) {
        when (answer) {
            "A" -> {
                if (color.equals("green")) {
                    binding.tvAnswerOne.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerOne.setBackgroundResource(R.drawable.bg_red)
                }
            }

            "B" -> {
                if (color.equals("green")) {
                    binding.tvAnswerTwo.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerTwo.setBackgroundResource(R.drawable.bg_red)
                }
            }

            "C" -> {
                if (color.equals("green")) {
                    binding.tvAnswerThree.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerThree.setBackgroundResource(R.drawable.bg_red)
                }
            }

            "D" -> {
                if (color.equals("green")) {
                    binding.tvAnswerFour.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerFour.setBackgroundResource(R.drawable.bg_red)
                }
            }

            "E" -> {
                if (color.equals("green")) {
                    binding.tvAnswerFive.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerFive.setBackgroundResource(R.drawable.bg_red)
                }
            }
        }

        checkingFutureAnswer(color, answer)
    }

    private fun checkingFutureAnswer(color: String, answer: String) {
        if (questionList[count].type.equals("single-choice")) {
            waitAndRestart()
            viewClickDisable()
        } else {
            if (color.equals("green")) {
                // answerMultiple.add(answer)
            } else {
                waitAndRestart()
                viewClickDisable()
            }
        }
    }

    private fun waitAndRestart() {
        counterDownTimer.cancel()
        val handlerNew = Handler()
        handlerNew.postDelayed({
            count++
            progressCount = 0
            if (count < (questionList.size)) {
                progressCount = 0
                viewClickEnable()
                setAllViews()
            } else {
                showDialog()
            }
        }, 2000)
    }


    private fun viewClickEnable() {
        binding.tvAnswerOne.isClickable = true
        binding.tvAnswerTwo.isClickable = true
        binding.tvAnswerThree.isClickable = true
        binding.tvAnswerFour.isClickable = true
        binding.tvAnswerFive.isClickable = true

    }

    private fun viewClickDisable() {

        binding.tvAnswerOne.isClickable = false
        binding.tvAnswerTwo.isClickable = false
        binding.tvAnswerThree.isClickable = false
        binding.tvAnswerFour.isClickable = false
        binding.tvAnswerFive.isClickable = false

    }

    private fun settingViewVisibility() {

        binding.ivLogo.visibility = View.GONE
        binding.tvAnswerOne.visibility = View.GONE
        binding.tvAnswerTwo.visibility = View.GONE
        binding.tvAnswerThree.visibility = View.GONE
        binding.tvAnswerFour.visibility = View.GONE
        binding.tvAnswerFive.visibility = View.GONE

        binding.tvAnswerOne.setBackgroundResource(R.drawable.bg_option)
        binding.tvAnswerTwo.setBackgroundResource(R.drawable.bg_option)
        binding.tvAnswerThree.setBackgroundResource(R.drawable.bg_option)
        binding.tvAnswerFour.setBackgroundResource(R.drawable.bg_option)
        binding.tvAnswerFive.setBackgroundResource(R.drawable.bg_option)
    }

    private fun showDialog() {

        val title = getString(R.string.app_name)
        val message =
            getString(R.string.label_end_question)
        MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
            .setTitle(title)
            .setIcon(R.drawable.ic_launcher_foreground)
            .setMessage(message)
            .setPositiveButton(
                getString(R.string.label_restart)
            ) { dialogInterface, i ->

            }
            .setNeutralButton(getString(R.string.label_finish)) { _, i ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (counterDownTimer != null) {
            counterDownTimer.cancel()
        }
    }
}