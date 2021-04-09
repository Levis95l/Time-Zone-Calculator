package com.example.timezonecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.timezonecalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Bind to activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val timeZones = resources.getStringArray(R.array.timeZones)

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeZones)
        binding.inputTimeZoneAutoCompleteTextView.setAdapter(arrayAdapter)
    }
}