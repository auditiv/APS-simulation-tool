package com.example.APS_simulation_tool.simulation

import com.example.APS_simulation_tool.components.*
import java.time.*

class Simulation(
    var patient: T1DPatient,
    var action: Action,
    var sensor: CGMSensor,
    var insulinPump: InsulinPump, // leave this as ideal Pump
    var algorithm: Algorithm,// for now lets leave it as a constant with constant rate... see later in the Sim
    var mealSchedule: Map<LocalTime, Double>,
    var startTime: LocalTime,
    var endTime: LocalTime,
    var BGMap: MutableMap<LocalTime, Double> = mutableMapOf(),
    var InsulinMap: MutableMap<LocalTime, Double> = mutableMapOf(),
    var MealMap: MutableMap<LocalTime, Double> = mutableMapOf(),


    ) {
    /**
     * This function implements the whole closed loop data processing.
     * It stores all blood sugar, insulin and carbs data into the corresponding maps.
     */
    fun discreteSimulation() {
        var currentTime = patient.startTime
        // check iff there are already some Meals ready
        val carbs = getCarbsFromSchedule(currentTime, mealSchedule)
        patient.carbsStorage += carbs  // Store carbs
        // get first value before simulation starts
        sensor.returnOnlyValuesInGivenFrequency(
            Pair(
                currentTime,
                sensor.injectNoise(patient.observation(patient.initialConditions[12]))
            ), BGMap
        ) //go through glucose monitoring

        InsulinMap.put(currentTime, action.insulin)
        sensor.returnOnlyValuesInGivenFrequency(
            Pair(currentTime, action.CHO),
            MealMap
        ) //go through glucose monitoring and filter some values out

        //duration in minutes
        val duration =
            java.time.Duration.between(startTime.plusMinutes(1), endTime).toMinutes().toInt() // shift by 1 min forward
        var minCounter = 0 //min counter
        while (minCounter < duration) {
            // Simulation step (assuming method exists in T1DPatient)
            val (BG, time) = patient.step(action)
            // Store BG values inject noise and sampling time
            sensor.returnOnlyValuesInGivenFrequency(
                Pair(time, sensor.injectNoise(BG)),
                BGMap
            ) //go through glucose monitoring and filter sampling rate
            // Insulin Dose:
            InsulinMap.put(time, this.action.insulin)

            // Meals Carbs:
            sensor.returnOnlyValuesInGivenFrequency(
                Pair(time, action.CHO),
                MealMap
            ) //go through carbs ingestion and filter some values out (sample frequency of sensor)

            // Check if there's food in the next minute
            val nextTime = time.plusMinutes(1)
            val carbs = getCarbsFromSchedule(nextTime, mealSchedule)
            patient.carbsStorage += carbs  // Store carbs

            // Calculate insulin (assuming method exists)
            val insulin = algorithm.getInsulinFromAlgorithm(time, minCounter)
            // inject noise for insulin dose
            action.insulin = insulinPump.injectNoise(insulin)

            // Increment the simulation minute counter
            action.CHO = patient.prepareNextMeal()

            minCounter += 1
        }
    }

    /**
     * This function looks if a meal is scheduled and returns the amount of carbs as Double
     */
    fun getCarbsFromSchedule(
        time: LocalTime,
        mealsMap: Map<LocalTime, Double>
    ): Double { //return amount of carbs && deletes taken carbs
        var carbs = 0.0
        if (mealsMap.containsKey(time)) {
            carbs = mealsMap.getValue(time)
        }
        return carbs
    }

}
