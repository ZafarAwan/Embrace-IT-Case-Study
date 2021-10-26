package com.embrace.casestudy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.embrace.casestudy.R
import com.embrace.casestudy.databinding.ActivityMainBinding
import com.embrace.casestudy.model.*
import com.embrace.casestudy.utils.ApiState
import com.embrace.casestudy.utils.NetworkComponents
import com.embrace.casestudy.viewModel.MainViewModel
import com.embrace.casestudy.viewModel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()
    private val mainRoomViewModel: RoomViewModel by viewModels()

    private lateinit var question: QuestionAnswers
    private var topScores: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getting question list from server or from room
        mainRoomViewModel.questionList.observe(this) {
            if (it == null) {
                callWebServiceForQuestionList()
            } else {
                var questionAnswers = QuestionAnswers(it.questionList)
                question = questionAnswers
            }
        }

        //getting Top scores from room
        mainRoomViewModel.topScores.observe(this) {
            if (it == null) {
                var topString = TopScores(0, 0)
                mainRoomViewModel.insertSingleScore(topString)
            } else {
                topScores = it.topScore
                scoreViewUpdate()
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel._questionStateFlow.collect {
                when (it) {
                    is ApiState.Loading -> {
                        binding.btnStart.isClickable = false
                        binding.progressBarCircle.visibility = View.VISIBLE
                    }
                    is ApiState.Failure -> {
                        binding.btnStart.isClickable = true
                        binding.progressBarCircle.visibility = View.GONE
                        Log.e("TAG", "onCreate: " + it.msg)
                    }
                    is ApiState.Success -> {
                        binding.btnStart.isClickable = true
                        binding.progressBarCircle.visibility = View.GONE
                        question = it.data
                        settingAnswerList()

                    }
                    is ApiState.Empty -> {
                    }
                }
            }
        }

        initMainView()
    }

    /***** calling webservice for getting Question List if internet is available  *****/
    private fun callWebServiceForQuestionList() {
        if (NetworkComponents.DETECT_INTERNET_CONNECTION(this)) {
            mainViewModel.getQuestionList()
        } else {
            NetworkComponents.showDialog(this)
        }
    }

    /***** add question list in the Room Data Base  *****/

    private fun addDataInRoomDataBase(questionList: List<Question>) {
        var questionAnswerListDB = QuestionAnswerListDB(0, questionList)
        mainRoomViewModel.insertAll(questionAnswerListDB)
    }

    /***** sending data from one activity to another  *****/
    private fun initMainView() {
        binding.tvTitle.text = resources.getString(R.string.app_name)
        scoreViewUpdate()

        binding.btnStart.setOnClickListener {
            if (question?.questionList?.isNotEmpty()) {
                val intent = Intent(this, QuestionAnswerActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(NetworkComponents.questionList, question)
                bundle.putInt(NetworkComponents.topScore, topScores)
                intent.putExtra(NetworkComponents.bundle, bundle)

                startActivity(intent)
            } else {
                callWebServiceForQuestionList()
            }
        }
    }

    /***** rearrange the list according to desire requirement  *****/
    private fun settingAnswerList() {

        for (item in question.questionList) {
            item.answersList = arrayListOf(
                item.answers?.A?.let {
                    AnswersMap(NetworkComponents.optionA, item.answers?.A!!)
                },
                item.answers?.B?.let {
                    AnswersMap(NetworkComponents.optionB, item.answers?.B!!)
                },
                item.answers?.C?.let {
                    AnswersMap(NetworkComponents.optionC, item.answers?.C!!)
                },
                item.answers?.D?.let {
                    AnswersMap(NetworkComponents.optionD, item.answers?.D!!)
                },
                item.answers?.E?.let {
                    AnswersMap(NetworkComponents.optionE, item.answers?.E!!)
                }
            )!!


            item.correctAnswerList = item.correctAnswer?.split(",")?.toList()!!
        }
        addDataInRoomDataBase(question.questionList)
    }


    private fun scoreViewUpdate() {
        binding.tvHighScores.text = "$topScores"
    }
}