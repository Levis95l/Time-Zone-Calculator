package com.example.timezonecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.timezonecalculator.databinding.ActivityMainBinding
import android.icu.text.DateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val timeZoneHandler = TimeZoneLister()
    private var inputDate: Long = 0
    private var inputTimeZone: Int? = null
    private var outputTimeZone: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Bind to activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //1st try: Manual list
        //val timeZones = resources.getStringArray(R.array.timeZonesShort)
        //val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeZones)

        //Better system: System supported timezones

        timeZoneHandler.updateAvailableZones()


        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeZoneHandler.displayNames)

        //Improving search for manual list: Custom auto complete
        //val autoCompleteAdapter = AutoCompleteAdapter(this, timeZones.asList())

        //Listener Time
        binding.inputTimeTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) updateDate()
        }

        //Listener Date
        binding.inputDateTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateDate()
                inputUpdated()
            }
        }

        //Set Time Zone Lists and listeners for them
        binding.inputTimeZoneAutoCompleteTextView.setAdapter(arrayAdapter)
        binding.inputTimeZoneAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            inputTimeZone = position
            inputUpdated()
        }

        binding.outputTimeZoneAutoCompleteTextView.setAdapter(arrayAdapter)
        binding.outputTimeZoneAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            outputTimeZone = position
            inputUpdated()
        }
    }

    private fun updateDate() {
        inputDate = System.currentTimeMillis()
    }

    private fun inputUpdated() {
        if (inputTimeZone != null && outputTimeZone != null) {
            val date = timeZoneHandler.convertTime(inputDate, timeZoneHandler.timeZones[inputTimeZone!!], timeZoneHandler.timeZones[outputTimeZone!!])

            binding.outputTimeTextView.text = "" + date
        }


    }
}