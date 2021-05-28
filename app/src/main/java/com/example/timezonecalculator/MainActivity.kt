package com.example.timezonecalculator

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.timezonecalculator.databinding.ActivityMainBinding
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val timeZoneHandler = TimeZoneHandler()
    private var inputDate: Long = 0
    private var inputTimeZone: String? = null
    private var outputTimeZone: String? = null

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

        //Get string arrays from strings.xml
        val ids = resources.getStringArray(R.array.timeZonesID)
        val nms = resources.getStringArray(R.array.timeZonesShort)
        val nml = resources.getStringArray(R.array.timeZonesLong)

        //Better system: System supported timezones
        timeZoneHandler.updateAvailableZones(ids, nms, nml)

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeZoneHandler.shortNames)

        //Improving search for manual list: Custom auto complete
        //val autoCompleteAdapter = AutoCompleteAdapter(this, timeZones.asList())

        //Listener Time
        /*
        binding.inputTimeTextInputEditText.setOnClickListener {
            updateDate()
            inputUpdated()
        }*/
        binding.inputTimeTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateDate()
                inputUpdated()
            }
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
            inputTimeZone = arrayAdapter.getItem(position).toString()
            inputUpdated()
        }

        binding.outputTimeZoneAutoCompleteTextView.setAdapter(arrayAdapter)
        binding.outputTimeZoneAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            outputTimeZone = arrayAdapter.getItem(position).toString()
            inputUpdated()
        }
    }

    private fun updateDate() {
        val cal = Calendar.getInstance()

        //Parse input string to hours and minutes
        var hour = 0; var minute = 0;
        val timeString = binding.inputTimeTextInputEditText.text
        val i: Int? = timeString?.indexOf(':')
        if (i != null && i>0) {
            Log.d("updateDate", "Checked if i=null. i =" + i)
            try {
                hour = timeString.substring(0, i).toInt()
                minute = timeString.substring(i+1).toInt()
            } catch (nfe: NumberFormatException) {
                Log.e(TAG, "Invalid time input. Should be in format \"[int]:[int]\".")
            }
        }

        cal.set(cal.get(1), cal.get(2), cal.get(5), hour, minute, 0)
        inputDate = cal.timeInMillis
    }

    private fun inputUpdated() {
        if (inputTimeZone != null && outputTimeZone != null) {

            val outputDate = timeZoneHandler.convertTime(inputDate, timeZoneHandler.findZoneByShortName(inputTimeZone!!), timeZoneHandler.findZoneByShortName(outputTimeZone!!))

            //Calendar.getInstance()
            val cal = Calendar.getInstance()
            cal.timeInMillis = outputDate
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val formattedDate = formatter.format(cal.time)

            binding.outputTimeTextView.text = "" + formattedDate
        }
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }
}