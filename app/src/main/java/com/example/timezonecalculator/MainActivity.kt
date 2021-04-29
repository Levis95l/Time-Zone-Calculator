package com.example.timezonecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.timezonecalculator.databinding.ActivityMainBinding
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log

private const val TAG = "MainActivity"

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
        val cal = Calendar.getInstance()

        //Parse input string to hours and minutes
        var hour = 0; var minute = 0;
        val timeString = binding.inputTimeTextInputEditText.text
        val i: Int? = timeString?.indexOf(':')
        if (i != null) {
            try {
                hour = timeString.substring(0, i).toInt()
                minute = timeString.substring(i+1).toInt()
            } catch (nfe: NumberFormatException) {
                Log.e(TAG, "Invalid time input. Should be in format \"[int]:[int]\".")
            }
        }

        cal.set(cal.get(1), cal.get(2), cal.get(5), hour, minute)
        inputDate = cal.timeInMillis
    }

    private fun inputUpdated() {
        if (inputTimeZone != null && outputTimeZone != null) {
            val outputDate = timeZoneHandler.convertTime(inputDate, timeZoneHandler.timeZones[inputTimeZone!!], timeZoneHandler.timeZones[outputTimeZone!!])
            //Calendar.getInstance()
            val cal = Calendar.getInstance()
            cal.timeInMillis = outputDate
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val formattedDate = formatter.format(cal.time)

            binding.outputTimeTextView.text = "" + formattedDate
        }


    }
}