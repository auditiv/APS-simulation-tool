package com.example.APS_simulation_tool.helpers
import kotlin.random.Random

/**
 * This class implements the Ideal Insulin Pump. It can be seen as an idealized placeholder for real world pumps.
 * It Provides the injectNoise function that makes exactly that.
 */
class InsulinPump(var errorRange: Double){

    fun perfectPump(value:Double):Double{
        return value
    }
    fun injectNoise(value: Double): Double {
        return value + Random.nextDouble(-errorRange, errorRange)
    }
}
