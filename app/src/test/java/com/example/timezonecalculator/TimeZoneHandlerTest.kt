package com.example.timezonecalculator

import junit.framework.TestCase

class TimeZoneHandlerTest : TestCase() {

    val timeZoneHandler = TimeZoneHandler()

    fun testGetTimeZones() {
        assertNotNull(timeZoneHandler.timeZones)
    }

    fun testGetShortNames() {
        assertNotNull(timeZoneHandler.shortNames)
    }

    fun testGetLongNames() {
        assertNotNull(timeZoneHandler.longNames)
    }

    fun testUpdateAvailableZones() {
        val ids = arrayOf("Universal", "America/Ensenada", "Pacific/Auckland")
        val nms = arrayOf("UTC (International)", "PST, PDT (US)", "NZST (NZ)")
        val nml = arrayOf("UTC - Universal Standard Time", "PST - Pacific Time (US)", "NZST - New Zealand Standard Time")
        timeZoneHandler.updateAvailableZones(ids, nms, nml)

        assertEquals("Universal", timeZoneHandler.timeZones[0].id)
        assertEquals("America/Ensenada", timeZoneHandler.timeZones[1].id)
        assertEquals("Pacific/Auckland", timeZoneHandler.timeZones[2].id)

        assertEquals("UTC (International)", timeZoneHandler.shortNames[0])
        assertEquals("PST, PDT (US)", timeZoneHandler.shortNames[1])
        assertEquals("NZST (NZ)", timeZoneHandler.shortNames[2])

        assertEquals("UTC - Universal Standard Time", timeZoneHandler.longNames[0])
        assertEquals("PST - Pacific Time (US)", timeZoneHandler.longNames[1])
        assertEquals("NZST - New Zealand Standard Time", timeZoneHandler.longNames[2])
    }

    fun testFindZoneByShortName() {}

    fun testConvertTime() {}
}