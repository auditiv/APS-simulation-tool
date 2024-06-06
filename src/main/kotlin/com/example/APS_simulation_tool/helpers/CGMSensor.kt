package com.example.APS_simulation_tool.helpers

import java.time.LocalTime
import kotlin.random.Random

/**
 * This Class implements the Continuous Glucouse Monitoring.
 * We abstract here the Sensor just into a device that has a certain
 * Error range (random) for each Measurement and a whished Sampling Rate in Minutes
 */
class CGMSensor(var samplingRateMinutes:Int,
                var errorRange: Double) {
    fun returnOnlyValuesInGivenFrequency(BGTimeValue:Pair<LocalTime, Double,>, BGMap:MutableMap<LocalTime,Double>){
        if(BGTimeValue.first.minute % this.samplingRateMinutes == 0){
            // value is part of CGM measurement
            BGMap.put(BGTimeValue.first, BGTimeValue.second) // store into Map -> gets plotted

        }
    }
    fun injectNoise(value:Double):Double{
        return value + Random.nextDouble(-errorRange, errorRange)
    }

}
