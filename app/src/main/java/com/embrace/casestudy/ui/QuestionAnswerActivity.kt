package com.embrace.casestudy.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.embrace.casestudy.R
import com.embrace.casestudy.databinding.ActivityQuestionAnswersBinding
import com.embrace.casestudy.model.Question
import com.embrace.casestudy.model.QuestionAnswers
import com.embrace.casestudy.model.TopScores
import com.embrace.casestudy.utils.NetworkComponents
import com.embrace.casestudy.viewModel.RoomViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionAnswerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var questionList: List<Question>
    private lateinit var binding: ActivityQuestionAnswersBinding
    private lateinit var counterDownTimer: CountDownTimer
    private var count = 0    //use for iteration the list of questions
    private var yScore = 0   //use for score of current user

    private var progressCount = 0
    private val mainRoomViewModel: RoomViewModel by viewModels()
    private var topScore: Int? = 0 //top score of quiz

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

    /***getting list from previous activity***/

    private fun getDataFromPreviousActivity() {

        val bundle = intent.getBundleExtra(NetworkComponents.bundle)
        topScore = bundle?.getInt(NetworkComponents.topScore, 0)
        var question: QuestionAnswers = bundle!!.getParcelable(NetworkComponents.questionList)!!
        questionList = question.questionList

        setAllViews()
    }

    /***setting view according to
    1.settingViewVisibility means reset view by-default
    2.settingDataInViews means setting data in views
    3.settingProgressBar means setting progress bar ***/

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

    /*** click listeners ***/

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.tvAnswerOne -> {
                checkingTheAnswer(NetworkComponents.optionA)
            }
            R.id.tvAnswerTwo -> {
                checkingTheAnswer(NetworkComponents.optionB)
            }
            R.id.tvAnswerThree -> {
                checkingTheAnswer(NetworkComponents.optionC)
            }
            R.id.tvAnswerFour -> {
                checkingTheAnswer(NetworkComponents.optionD)
            }
            R.id.tvAnswerFive -> {
                checkingTheAnswer(NetworkComponents.optionE)
            }
        }
    }

    /***check answer is correct or wrong then change background***/

    private fun checkingTheAnswer(answer: String) {
        var correctAnswer = questionList[count].correctAnswer

        if (correctAnswer?.contains(answer) == true) {
            yScore += questionList[count].score!!
            changeBackground(answer, NetworkComponents.green)
        } else {
            changeBackground(correctAnswer!!, NetworkComponents.green)
            changeBackground(answer, NetworkComponents.red)
        }
    }

    /***setting background resource according to answer***/

    private fun changeBackground(answer: String, color: String) {
        when (answer) {
            NetworkComponents.optionA -> {
                if (color.equals(NetworkComponents.green)) {
                    binding.tvAnswerOne.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerOne.setBackgroundResource(R.drawable.bg_red)
                }
            }

            NetworkComponents.optionB -> {
                if (color.equals(NetworkComponents.green)) {
                    binding.tvAnswerTwo.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerTwo.setBackgroundResource(R.drawable.bg_red)
                }
            }

            NetworkComponents.optionC -> {
                if (color.equals(NetworkComponents.green)) {
                    binding.tvAnswerThree.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerThree.setBackgroundResource(R.drawable.bg_red)
                }
            }

            NetworkComponents.optionD -> {
                if (color.equals(NetworkComponents.green)) {
                    binding.tvAnswerFour.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerFour.setBackgroundResource(R.drawable.bg_red)
                }
            }

            NetworkComponents.optionE -> {
                if (color.equals(NetworkComponents.green)) {
                    binding.tvAnswerFive.setBackgroundResource(R.drawable.bg_green)
                } else {
                    binding.tvAnswerFive.setBackgroundResource(R.drawable.bg_red)
                }
            }
        }

        checkingFutureAnswer(color, answer)
    }

    /***checking single or multiple answer choice and decide reset view or still waiting***/

    private fun checkingFutureAnswer(color: String, answer: String) {
        if (questionList[count].type.equals(NetworkComponents.single_choice)) {
            waitAndRestart()
            viewClickDisable()
        } else {
            if (color.equals(NetworkComponents.green)) {
                // answerMultiple.add(answer)
            } else {
                waitAndRestart()
                viewClickDisable()
            }
        }
    }

    /***wait 2 second and again enable views and set data ***/

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

    /***enable view option***/

    private fun viewClickEnable() {
        binding.tvAnswerOne.isClickable = true
        binding.tvAnswerTwo.isClickable = true
        binding.tvAnswerThree.isClickable = true
        binding.tvAnswerFour.isClickable = true
        binding.tvAnswerFive.isClickable = true

    }

    /***disable view option***/

    private fun viewClickDisable() {

        binding.tvAnswerOne.isClickable = false
        binding.tvAnswerTwo.isClickable = false
        binding.tvAnswerThree.isClickable = false
        binding.tvAnswerFour.isClickable = false
        binding.tvAnswerFive.isClickable = false

    }

    /***view visibilities set***/

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

    /***show dialog when Quiz is finished***/

    private fun showDialog() {

        val title = getString(R.string.app_name)
        val message =
            getString(R.string.label_end_question)
        MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.label_finish)) { _, i ->
                updateScoreInRoom(yScore)
                finish()
            }
            .setCancelable(false)
            .show()
    }

    /***update score in the room***/

    private fun updateScoreInRoom(yScore: Int) {
        if (yScore > topScore!!) {
            var topScores = TopScores(1, yScore)
            mainRoomViewModel.updateSingleScore(topScores)
        }
    }

    /***counter down timer cancel when view destroy***/

    override fun onDestroy() {
        super.onDestroy()
        if (counterDownTimer != null) {
            counterDownTimer.cancel()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (counterDownTimer != null) {
            counterDownTimer.cancel()
        }
    }
}