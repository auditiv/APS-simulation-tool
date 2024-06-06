package com.example.APS_simulation_tool.helpers
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


        ){
    fun discreteSimulation() {
        var currentTime = startTime
        //duration in minutes
        val duration = java.time.Duration.between(startTime, endTime).toMinutes().toInt() //min

        var minCounter = 0 //min counter
        while (minCounter < duration) {
            // Simulation step (assuming method exists in T1DPatient)
            val (BG, time) = patient.step(action)
            println("BG value: $BG at time: $time")
            println("Action.CHO: ${action.CHO} with insulin: ${action.insulin}")
            // STORE THE BG : TIME && ACTION.INSULIN : TIME

            sensor.returnOnlyValuesInGivenFrequency(Pair(time, BG),BGMap) //go through CGM
            InsulinMap.put(time, action.insulin)
            MealMap.put(time, this.action.CHO)



            // Check if there's food in the next minute
            val nextTime = time.plusMinutes(1)
            val carbs = getCarbsFromSchedule(nextTime, mealSchedule)
            patient.carbsStorage += carbs  // Store carbs

            // Calculate insulin (assuming method exists)
            val insulin = algorithm.getInsulinFromAlgorithm(time, minCounter)

            action.insulin = insulin
            action.CHO = patient.prepareNextMeal()  // Prepare the next meal (assuming method exists)

            // Increment the simulation minute counter

            minCounter += 1
        }
    }

    fun getCarbsFromSchedule(time: LocalTime, mealsMap: Map<LocalTime, Double>):Double{ //return amount of carbs && deletes taken carbs
        var carbs = 0.0
        if(mealsMap.containsKey(time)){
            carbs = mealsMap.getValue(time)
        }
        return carbs
    }

}
