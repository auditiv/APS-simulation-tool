package com.example.APS_simulation_tool.components

import java.time.LocalTime
import kotlin.random.Random

/**
 * This Class implements the Continuous Glucouse Monitoring.
 * We abstract here the Sensor just into a device that has a certain
 * error range (random) for each Measurement and a whished Sampling Rate in Minutes
 */
class CGMSensor(var samplingRateMinutes:Int,
                var errorRange: Double) {
    /**
     * This function takes a value pair and checks if the value is in the time grid (sampling rate)
     * of the sensor and stores only the values that are taken in the sampling rate.
     */
    fun returnOnlyValuesInGivenFrequency(BGTimeValue:Pair<LocalTime, Double,>, BGMap:MutableMap<LocalTime,Double>){
        if(BGTimeValue.first.minute % this.samplingRateMinutes == 0){
            // value is part of CGM measurement
            BGMap.put(BGTimeValue.first, BGTimeValue.second) // store into Map -> gets plotted

        }
    }
    fun injectNoise(value:Double):Double{
        if (errorRange != 0.0){ //
        return value + Random.nextDouble(-errorRange, errorRange)}
        else{
            return value
        }
    }

}
