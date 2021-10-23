package com.embrace.casestudy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.embrace.casestudy.databinding.ActivityQuestionAnswersBinding
import com.embrace.casestudy.model.QuestionAnswers

class QuestionAnswerActivity : AppCompatActivity() {
    private lateinit var question: QuestionAnswers
    private lateinit var binding: ActivityQuestionAnswersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionAnswersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.getBundleExtra("Bundle")

        question = bundle!!.getParcelable("questionList")!!


    }

}