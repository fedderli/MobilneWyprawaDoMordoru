package com.example.travelplaner

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {



    var date="dd.mm.rrrr"
    var time="gg:mm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = findViewById<EditText>(R.id.myName)
        val raceSpinner: Spinner = findViewById<Spinner>(R.id.RaceSpinner)
        val race = arrayOf("Krasnolud", "Elf", "Hobbit")

        val showDatePickerButton: Button = findViewById<Button>(R.id.DateButton)
        showDatePickerButton.setOnClickListener {

            showDatePickerDialog()
        }

        val showTimePickerButton: Button = findViewById<Button>(R.id.TimeButton)
        showTimePickerButton.setOnClickListener {
            showTimePickerDialog()
        }


        val dateAndTime = findViewById<TextView>(R.id.DateAndTimeText)


        val elfSwitch = findViewById<Switch>(R.id.ElfSwitch)
    }


    private fun showTimePickerDialog() {
        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)
        time = "$hour:$minute"
    }

    private fun showDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        date = "$day.$month.$year"
        Log.i("w", date)
    }


}



