package com.example.timezonecalculator

import android.content.res.Resources
import android.icu.util.TimeZone
import android.util.Log
import kotlin.collections.ArrayList

private const val TAG = "TimeZoneHandler"

class TimeZoneHandler {
    val timeZones = ArrayList<TimeZone>()
    val shortNames = ArrayList<String>()
    val longNames = ArrayList<String>()

    fun updateAvailableZones(ids: Array<String>, nms: Array<String>, nml: Array<String>) {
        for (i in ids.indices) {
            timeZones.add(TimeZone.getTimeZone(ids.get(i)))
            shortNames.add(nms[i])
            longNames.add(nml[i])
        }
    }
    /*
    //Lists all available time zones. Too much. Duplicates etc.

    fun updateAvailableZones() {
        val allZoneIDs = TimeZone.getAvailableIDs()
        //val filteredZoneIDs = ArrayList<String>()

        //For log
        val date = System.currentTimeMillis()
        var num = 1

        for (id in allZoneIDs) {
            //Skip short 3 letter time zone ID's, they are deprecated
            //if (id.length > 3) {
            if (true) {
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

                //Write timezone info to Log, so we can check which ones are correct
                Log.d("TZ$num", "Short:" + zone.getDisplayName(false, 0) + " ID:" + id + "\nName:" + zone.displayName + " Offset:" + zone.getOffset(date))
                num++

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
    }*/

    //
    fun findZoneByShortName(shortName: String): TimeZone {
        val i = shortNames.indexOf(shortName)
        return timeZones.get(i)
    }

    //
    fun convertTime(date: Long, tz1: TimeZone, tz2: TimeZone): Long {

        Log.d(TAG, "Current date is $date")
        Log.d(TAG, "Input timezone is " + tz1.id + " and offset " + tz1.getOffset(date))
        Log.d(TAG, "Output timezone is "+ tz2.id + " and offset " + tz2.getOffset(date) + "\n")
        return date - tz1.getOffset(date) + tz2.getOffset(date)
    }
}