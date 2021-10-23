package com.embrace.casestudy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.embrace.casestudy.R
import com.embrace.casestudy.databinding.ActivityMainBinding
import com.embrace.casestudy.model.QuestionAnswers
import com.embrace.casestudy.utils.ApiState
import com.embrace.casestudy.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var question: QuestionAnswers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMainView()

        mainViewModel.getQuestionList()

        lifecycleScope.launchWhenStarted {
            mainViewModel._questionStateFlow.collect {
                when (it) {
                    is ApiState.Loading -> {

                    }
                    is ApiState.Failure -> {

                        Log.e("TAG", "onCreate: " + it.msg)
                    }
                    is ApiState.Success -> {
                        question = it.data
                    }
                    is ApiState.Empty -> {

                    }
                }
            }
        }

    }

    private fun initMainView() {

        binding.tvTitle.text = resources.getString(R.string.app_name)
        binding.tvHighScores.text = "00"

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, QuestionAnswerActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("questionList", question)
            intent.putExtra("Bundle", bundle)
            startActivity(intent)
        }
    }
}