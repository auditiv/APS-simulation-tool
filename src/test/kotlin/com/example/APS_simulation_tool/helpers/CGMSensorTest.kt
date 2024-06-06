package com.example.APS_simulation_tool.helpers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalTime

class CGMSensorTest{
    @Test
    fun `check if the sampling frequency is being hold consitently`(){
        //WHEN
        var sensor = CGMSensor(5,0.1)
        var bgList = mutableListOf(LocalTime.of(6,28) to 140.0034,
                                LocalTime.of(6,29) to 140.1134,
                                LocalTime.of(6,30) to 141.0,
                                LocalTime.of(6,31) to 142.0034)
        // emptyMap for Storing the Data
        var bgMap = mutableMapOf<LocalTime,Double>()
        // THEN
        for(BGTimePair in bgList) {
            sensor.returnOnlyValuesInGivenFrequency(BGTimePair, bgMap)
        }
        //ASSERT

        assertEquals( 141.0, bgMap[LocalTime.of(6,30)])
    }
}