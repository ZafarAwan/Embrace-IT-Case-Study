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
import java.util.Collections.shuffle

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
                if (progressCount < 11) {
                    progressCount += 1
                    binding.progressBar.progress = progressCount
                    binding.progressStatus.text =
                        "$progressCount / ${binding.progressBar.max}"
                } else {
                    counterDownTimer.cancel()
                }
            }

            override fun onFinish() {
                counterDownTimer.cancel()
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

        //remove null option data
        questionList[count].answersList.removeAll(listOf(null))

        //rearrange the answer list every time
        shuffle(questionList[count].answersList)

        //showing the views according to answer list size
        when (questionList[count].answersList.size) {
            2 -> {
                viewTwoShow()
            }
            3 -> {
                viewThreeShow()
            }
            4 -> {
                viewFourShow()
            }
            5 -> {
                viewFiveShow()
            }
        }

    }

    private fun viewFiveShow() {
        viewTwoShow()
        viewThreeShow()
        viewFourShow()
        questionList[count].answersList[4].let {
            binding.tvAnswerFive.visibility = View.VISIBLE
            binding.tvAnswerFive.text = it?.value.toString()
        }

    }

    private fun viewFourShow() {
        viewTwoShow()
        viewThreeShow()
        questionList[count].answersList[3].let {
            binding.tvAnswerFour.visibility = View.VISIBLE
            binding.tvAnswerFour.text = it?.value.toString()
        }
    }

    private fun viewThreeShow() {
        viewTwoShow()
        questionList[count].answersList[2].let {
            binding.tvAnswerThree.visibility = View.VISIBLE
            binding.tvAnswerThree.text = it?.value.toString()
        }

    }

    private fun viewTwoShow() {
        questionList[count].answersList[0].let {
            binding.tvAnswerOne.visibility = View.VISIBLE
            binding.tvAnswerOne.text = it?.value.toString()
        }

        questionList[count].answersList[1].let {
            binding.tvAnswerTwo.visibility = View.VISIBLE
            binding.tvAnswerTwo.text = it?.value.toString()
        }

    }

    /*** click listeners ***/

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.tvAnswerOne -> {
                checkingTheAnswer(
                    NetworkComponents.optionA,
                    questionList[count].answersList[0]?.key.toString()
                )
            }
            R.id.tvAnswerTwo -> {
                checkingTheAnswer(
                    NetworkComponents.optionB,
                    questionList[count].answersList[1]?.key.toString()
                )
            }
            R.id.tvAnswerThree -> {
                checkingTheAnswer(
                    NetworkComponents.optionC,
                    questionList[count].answersList[2]?.key.toString()
                )
            }
            R.id.tvAnswerFour -> {
                checkingTheAnswer(
                    NetworkComponents.optionD,
                    questionList[count].answersList[3]?.key.toString()
                )
            }
            R.id.tvAnswerFive -> {
                checkingTheAnswer(
                    NetworkComponents.optionE,
                    questionList[count].answersList[4]?.key.toString()
                )
            }
        }
    }

    /***check answer is correct or wrong then change background***/

    private fun checkingTheAnswer(pos: String, answer: String) {
        var correctAnswer = questionList[count].correctAnswer

        // check single choice or multiple answer if multiple then
        // divide the score of question divider by multiple choice option size

        if (correctAnswer?.contains(answer) == true) {
            yScore += if (!questionList[count].type.equals(NetworkComponents.single_choice)) {
                (questionList[count].score!! / questionList[count].correctAnswerList.size)
            } else {
                questionList[count].score!!
            }

            changeBackground(answer, NetworkComponents.green, pos)
            checkingFutureAnswer(NetworkComponents.green, answer)
        } else {
            // if answer is wrong then check the correct answers view position
            for (i in 0 until questionList[count].answersList.size) {
                if (correctAnswer!!.contains(questionList[count].answersList[i]?.key.toString())) {

                    when (i) {
                        0 -> {
                            setBackgroundResource(
                                correctAnswer!!,
                                NetworkComponents.optionA, answer, pos
                            )

                            break
                        }
                        1 -> {
                            setBackgroundResource(
                                correctAnswer!!,
                                NetworkComponents.optionB, answer, pos
                            )
                            break
                        }
                        2 -> {
                            setBackgroundResource(
                                correctAnswer!!,
                                NetworkComponents.optionC, answer, pos
                            )
                            break
                        }
                        3 -> {
                            setBackgroundResource(
                                correctAnswer!!,
                                NetworkComponents.optionD, answer, pos
                            )
                            break
                        }
                        4 -> {
                            setBackgroundResource(
                                correctAnswer!!,
                                NetworkComponents.optionE, answer, pos
                            )
                            break
                        }
                    }
                }
            }

        }
    }

    /******* if single choice then getting view position and update view
    but if multiple choice then again getting the correct answers views positions and update it *******/

    private fun setBackgroundResource(
        correctAnswer: String,
        posCorrect: String,
        yAnswer: String,
        posWrong: String
    ) {
        changeBackground(yAnswer, NetworkComponents.red, posWrong)
        if (!questionList[count].type.equals(NetworkComponents.single_choice)) {

            for (i in 0 until questionList[count].answersList.size) {
                if (correctAnswer!!.contains(questionList[count].answersList[i]?.key.toString())) {

                    when (i) {
                        0 -> {
                            changeBackground(
                                correctAnswer,
                                NetworkComponents.green,
                                NetworkComponents.optionA
                            )
                        }
                        1 -> {
                            changeBackground(
                                correctAnswer,
                                NetworkComponents.green,
                                NetworkComponents.optionB
                            )
                        }
                        2 -> {
                            changeBackground(
                                correctAnswer,
                                NetworkComponents.green,
                                NetworkComponents.optionC
                            )
                        }
                        3 -> {
                            changeBackground(
                                correctAnswer,
                                NetworkComponents.green,
                                NetworkComponents.optionD
                            )
                        }
                        4 -> {
                            changeBackground(
                                correctAnswer,
                                NetworkComponents.green,
                                NetworkComponents.optionE
                            )
                        }
                    }
                }
            }
        } else {
            changeBackground(
                correctAnswer,
                NetworkComponents.green,
                posCorrect
            )
        }

        checkingFutureAnswer(NetworkComponents.red, yAnswer)
    }

    /***setting background resource according to answer***/

    private fun changeBackground(answer: String, color: String, pos: String) {
        when (pos) {
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

    }

    /***checking single or multiple answer choice and decide reset view or still waiting***/

    private fun checkingFutureAnswer(color: String, answer: String) {
        binding.tvYScore.text = "$yScore"
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