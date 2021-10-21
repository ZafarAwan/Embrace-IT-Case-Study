package com.embrace.casestudy.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.embrace.casestudy.R
import com.embrace.casestudy.databinding.ActivityMainBinding
import com.embrace.casestudy.utils.ApiState
import com.embrace.casestudy.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMainView()

    }

    private fun initMainView() {

        binding.tvTitle.text = resources.getString(R.string.app_name)
        binding.tvHighScores.text = "00"

        binding.btnStart.setOnClickListener {

        }

    }
}