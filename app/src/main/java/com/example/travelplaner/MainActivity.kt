package com.example.travelplaner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    var date = "dd.mm.rrrr"
    var time = "gg:mm"
    var marchTime = 0

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

        val raceSpinner: Spinner = findViewById(R.id.RaceSpinner)
        val races = arrayOf("Krasnolud", "Elf", "Hobbit")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, races)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        raceSpinner.adapter = adapter

        val raceImage = findViewById<ImageView>(R.id.RaceImage)

        raceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                when (parent.getItemAtPosition(position).toString()) {
                    "Krasnolud" -> raceImage.setImageResource(R.drawable.krasnolud)
                    "Elf" -> raceImage.setImageResource(R.drawable.elf)
                    "Hobbit" -> raceImage.setImageResource(R.drawable.hobbit)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val showDatePickerButton: Button = findViewById(R.id.DateButton)
        showDatePickerButton.setOnClickListener { showDatePickerDialog() }

        val showTimePickerButton: Button = findViewById(R.id.TimeButton)
        showTimePickerButton.setOnClickListener { showTimePickerDialog() }

        val elfSwitch = findViewById<Switch>(R.id.ElfSwitch)

        val torch = findViewById<CheckBox>(R.id.MyTorch)
        val bread = findViewById<CheckBox>(R.id.MyBread)
        val coat = findViewById<CheckBox>(R.id.MyCoat)

        val priorityGroup = findViewById<RadioGroup>(R.id.priorityMarch)

        val timeSeekBar = findViewById<SeekBar>(R.id.timeMarch)
        val timeText = findViewById<TextView>(R.id.timeMarchText)

        timeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                timeText.text = "Marsz trwa: $progress min."
                marchTime = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val chronometer = findViewById<Chronometer>(R.id.treningTime)
        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        var running = false
        var pauseOffset: Long = 0

        startButton.setOnClickListener {
            if (!running) {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                running = true
            }
        }

        stopButton.setOnClickListener {
            if (running) {
                chronometer.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                running = false
            }
        }

        val ratingBar = findViewById<RatingBar>(R.id.teamMorale)

        val countdownButton = findViewById<Button>(R.id.btnStartCountdown)
        val progressBar = findViewById<ProgressBar>(R.id.countdownDeparture)
        val summaryText = findViewById<TextView>(R.id.summary)

        val totalTime = 4_000L
        val interval = 1_000L

        countdownButton.setOnClickListener {
            progressBar.progress = progressBar.max
            object : CountDownTimer(totalTime, interval) {
                override fun onTick(millisUntilFinished: Long) {
                    val stepsLeft = (millisUntilFinished / interval).toInt()
                    progressBar.progress = stepsLeft
                }

                override fun onFinish() {
                    progressBar.progress = 0

                    val playerName = name.text.toString()
                    val selectedRace = raceSpinner.selectedItem.toString()
                    val elfselected = if (elfSwitch.isChecked) "tak" else "nie"

                    val items = mutableListOf<String>()
                    if (torch.isChecked) items.add("Pochodnia")
                    if (bread.isChecked) items.add("Lembasy")
                    if (coat.isChecked) items.add("Płaszcz elfów")
                    val selectedItems = items.joinToString(", ")

                    val priorityId = priorityGroup.checkedRadioButtonId
                    val selectedRadio = findViewById<RadioButton>(priorityId)
                    val selectedPriority = selectedRadio.text.toString()

                    // Poprawka: pobieramy czas bezpośrednio z chronometru
                    val trainingTime = chronometer.text.toString()

                    val morale = ratingBar.rating

                    summaryText.text = """
                        Imię: $playerName
                        Rasa: $selectedRace
                        Data: $date Godzina: $time
                        Ścieżki elfów: $elfselected
                        Wyposażenie: $selectedItems
                        Priorytet Marszu: $selectedPriority
                        Marsz trwa: $marchTime min.
                        Czas Treningu: $trainingTime
                        Morale: $morale
                    """
                }
            }.start()
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                time = String.format("%02d:%02d", selectedHour, selectedMinute)
                updateTimeAndDate()
            },
            hour,
            minute,
            true
        )
        timePicker.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                date = String.format("%02d.%02d.%04d", selectedDay, selectedMonth + 1, selectedYear)
                updateTimeAndDate()
            },
            year,
            month,
            day
        )
        datePicker.show()
    }

    private fun updateTimeAndDate() {
        val dateAndTime = findViewById<TextView>(R.id.DateAndTimeText)
        dateAndTime.text = "Data: $date Godzina: $time"
    }
}
