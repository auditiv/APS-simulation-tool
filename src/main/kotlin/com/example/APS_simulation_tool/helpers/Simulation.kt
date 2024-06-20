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
        var currentTime = patient.startTime
        // check iff there are already some Meals ready
        val carbs = getCarbsFromSchedule(currentTime, mealSchedule)
        patient.carbsStorage += carbs  // Store carbs
        // get first value before simulation starts
        sensor.returnOnlyValuesInGivenFrequency(Pair(currentTime, sensor.injectNoise(patient.observation(patient.initialConditions[12]))),BGMap) //go through glucose monitoring

        InsulinMap.put(currentTime, action.insulin)
        sensor.returnOnlyValuesInGivenFrequency(Pair(currentTime, action.CHO),MealMap) //go through glucose monitoring

        //MealMap.put(currentTime, this.action.CHO)

        //duration in minutes
        val duration = java.time.Duration.between(startTime.plusMinutes(1), endTime).toMinutes().toInt() // shift by 1 min forward
        var minCounter = 0 //min counter
        while (minCounter < duration) {
            // Simulation step (assuming method exists in T1DPatient)
            val (BG, time) = patient.step(action)
            println("BG value: $BG at time: $time")
            println("Action.CHO: ${action.CHO} with insulin: ${action.insulin}")
            // STORE THE BG : TIME && ACTION.INSULIN : TIME
            // Store BG values inject noise and sampling time
            sensor.returnOnlyValuesInGivenFrequency(Pair(time, sensor.injectNoise(BG)),BGMap) //go through glucose monitoring and filter sampling rate
            // Insulin Dose:
            //sensor.returnOnlyValuesInGivenFrequency(Pair(time, action.insulin),InsulinMap) //go through glucose monitoring
            InsulinMap.put(time, this.action.insulin)

            // Meals Carbs:
            //sensor.returnOnlyValuesInGivenFrequency(Pair(currentTime, action.CHO),MealMap) //go through glucose monitoring
            MealMap.put(time, this.action.CHO)
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

    fun getCarbsFromSchedule(time: LocalTime, mealsMap: Map<LocalTime, Double>):Double{ //return amount of carbs && deletes taken carbs
        var carbs = 0.0
        if(mealsMap.containsKey(time)){
            carbs = mealsMap.getValue(time)
        }
        return carbs
    }

}
