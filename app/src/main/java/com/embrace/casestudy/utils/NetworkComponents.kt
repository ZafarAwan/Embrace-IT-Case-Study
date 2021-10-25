package com.embrace.casestudy.utils

import android.content.Context
import com.embrace.casestudy.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spacenextdoor.detectConnection.ConnectionDetector

object NetworkComponents {

    var baseUrl: String = "https://mocki.io/v1/"

    var questionList: String =
        "questionList"//sent list from one activity to another intent reference name
    var topScore: String = "topScore"//top Score one activity to another intent reference name
    var bundle: String = "bundle"// bundle from one activity to another intent reference name
    var green: String = "green"//mean answer is correct and change background green
    var red: String = "red"//mean answer is wrong and change background green
    var optionA: String = "A"//mean answer is A
    var optionB: String = "B"//mean answer is B
    var optionC: String = "C"//mean answer is C
    var optionD: String = "D"//mean answer is D
    var optionE: String = "E"//mean answer is E
    var single_choice: String = "single-choice"//mean answer is single choice

    fun DETECT_INTERNET_CONNECTION(context: Context): Boolean {
        val cd = ConnectionDetector(context)
        return cd.isConnectingToInternet
    }

    fun showDialog(context: Context) {
        val title = context.getString(R.string.alert)
        val message =
            context.getString(R.string.no_Internet)
        MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                context.getString(R.string.label_ok)
            ) { dialogInterface, i ->

            }
            .setCancelable(false)
            .show()
    }
}