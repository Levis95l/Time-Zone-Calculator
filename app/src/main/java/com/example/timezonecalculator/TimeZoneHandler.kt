package com.example.timezonecalculator

import android.icu.util.TimeZone
import android.util.Log
import kotlin.collections.ArrayList

private const val TAG = "TimeZoneHandler"

class TimeZoneLister {
    val timeZones = ArrayList<TimeZone>()
    val displayNames = ArrayList<String>()

    fun updateAvailableZones() {
        val allZoneIDs = TimeZone.getAvailableIDs()
        //val filteredZoneIDs = ArrayList<String>()

        for (id in allZoneIDs) {
            //Skip short 3 letter time zone ID's, they are deprecated
            if (id.length > 3) {
                val equivalentZoneCount = TimeZone.countEquivalentIDs(id)
                var equivalentZoneExists = false
                //Go through all the equivalent time zones #1
                /*
                for (i in 0 until equivalentZoneCount) {
                    //If one of them already exists in filteredZoneIDs, don't add new zone
                    if (filteredZoneIDs.contains(TimeZone.getEquivalentID(id, i))) {
                        equivalentZoneExists = true;
                        break;
                    }
                }
                */
                val zone = TimeZone.getTimeZone(id)

                //Go through all existing time zones and check equivalence #2
                for (fz in timeZones) {
                    //Duplicates even when checking equivalence correctly
                    //if (fz.hasSameRules(zone)) equivalentZoneExists = true

                    //So let's just check name equivalence #3
                    if (fz.displayName == zone.displayName) equivalentZoneExists = true
                }

                if (!equivalentZoneExists) {
                    //Add new time zone to the lists
                    timeZones.add(zone)
                    displayNames.add(zone.getDisplayName(false, 0) + " " + zone.displayName)
                }
            }
        }
    }

    //
    fun convertTime(date: Long, tz1: TimeZone, tz2: TimeZone): Long {

        Log.d(TAG, "Current date is $date")
        Log.d(TAG, "Input timezone offset is " + tz1.getOffset(date))
        Log.d(TAG, "Output timezone offset is " + tz2.getOffset(date) + "\n")
        return date - tz1.getOffset(date) + tz2.getOffset(date)
    }
}