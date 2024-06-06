package com.example.APS_simulation_tool.controllers

import com.example.APS_simulation_tool.helpers.*
import com.example.APS_simulation_tool.models.ParametersView
import com.example.APS_simulation_tool.services.ParameterService
import com.example.APS_simulation_tool.services.SimulationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
class SimulationController(@Autowired var parameterService: ParameterService, var simService: SimulationService){
    @GetMapping("/runSimulation")
    fun runSimulation(model: Model,  redirectAttributes: RedirectAttributes): String {
        // find IDs of all Simulations where: readyToPlot = True -> store them in a List
        val listOfMultipleSimulationParameters = parameterService.settingParametersRepo.findAll()
        // create Simulation for each Id with its parameters where readyToPlot is set to TRUE
        var listofParaViewFromDB = mutableListOf<ParametersView>()
        for (simulation in  listOfMultipleSimulationParameters){
            if (simulation == null){
                println("no simulation was selected")
                return "redirect:/"
            }

            if(simService.getLineById(simulation.id).readyToPlot) {// check if it should be plotted
                listofParaViewFromDB.add(ParametersView(
                        SerializeHelper.deserializeToListOfParameter(simulation.virtualPatientParams),
                        SerializeHelper.deserializeToListOfParameter(simulation.algorithmParams),
                        SerializeHelper.deserializeToListOfParameter(simulation.sensorParams),
                        SerializeHelper.deserializeToListOfParameter(simulation.insulinPumpParams),
                        SerializeHelper.deserializeToListOfMealParameter(simulation.mealsParams),
                        SerializeHelper.deserializeToListOfTimeParameter(simulation.generalParams),
                        simulation.id))
            }
        }
        for (p in listofParaViewFromDB) {
            println(p.id)
        }
        // get general params
        var TimesParametersMap = PatientHelper().createHashMapFromTimeParameterList(listofParaViewFromDB[0].generalParams!!)
        val startTime = TimesParametersMap.getValue("Simulation Start")
        val endTime = TimesParametersMap.getValue("Simulation End")
        // CREATE patient
        // Hashmap
        var patientParametersMap = PatientHelper().createHashMapFromPatientParameterList(listofParaViewFromDB[0].virtualPatientParams!!)
        // initial condition
        var initConditions = PatientHelper().createInitialConditionsForPatient()
        var patient = T1DPatient(patientParametersMap,initConditions,startTime = startTime)
        // CREATE Insulin Pump
        var insulinMap = PatientHelper().createHashMapFromPatientParameterList(listofParaViewFromDB[0].insulinPumpParams!!)
        var insulinPump = InsulinPump(insulinMap.getValue("abserr_basal"))
        // CREATE Algorithm
        var algorithmMap = PatientHelper().createHashMapFromPatientParameterList(listofParaViewFromDB[0].algorithmParams!!)
        var algorithm = Algorithm() // here we should be able to chose between basal bolus and  oref0
        // CREATE Sensor
        var sensorMap = PatientHelper().createHashMapFromPatientParameterList(listofParaViewFromDB[0].sensorParams!!)
        var sensor = CGMSensor(sensorMap.getValue("samplingtime").toInt(), sensorMap.getValue("error_range"))
        // CREATE THE SIMULATION:
        //mealsmap
        var mealSchedule = PatientHelper().createMealsMapFromMealParameterList(listofParaViewFromDB[0].mealsParams!!)
        var sim = Simulation(patient = patient,
                action = Action(0.0,0.0), // actions should be working fine!
                sensor=sensor,
                insulinPump = insulinPump, // work through class again
                algorithm = algorithm,
                mealSchedule = mealSchedule, // also here load from BD
                startTime = startTime, // load in from params.general
                endTime = endTime)  // load in from params.general
        //ASSERT
        sim.discreteSimulation()
        // run them and store them in a Huge Dictionary

        // get the calculated Data:
        var mockBG = sim.BGMap.values.toList() // len = 10
        var mockTime = sim.BGMap.keys.toList()
        val insulinData = sim.InsulinMap.values.toList()
        val insulinTime = sim.InsulinMap.keys.toList()
        val mealData = sim.MealMap.values.toList()
        println(mealData[1])

        // LocalTime -> String
        var mockTimeStr = mutableListOf<String>()
        for (time in mockTime){
            mockTimeStr.add(time.toString())
        }
        mockTimeStr.toList()

        var insulinTimeStr = mutableListOf<String>()

        for (time in insulinTime){
            insulinTimeStr.add(time.toString())
        }
        //add UpperBound
        var BgUpperData = mutableListOf<Double>()

        //add lower bound
        var BgLowerData = mutableListOf<Double>()
        for (value in mockBG){
            BgUpperData.add(value+2*sensor.errorRange)
            BgLowerData.add(value-2*sensor.errorRange)
        }

        // add to model
        // Adding to redirect attributes instead of model
        redirectAttributes.addFlashAttribute("mockBG", mockBG)
        redirectAttributes.addFlashAttribute("BGUpperData", BgUpperData)
        redirectAttributes.addFlashAttribute("BGLowerData", BgLowerData)
        redirectAttributes.addFlashAttribute("mealData", mealData)
        redirectAttributes.addFlashAttribute("mocktime", mockTimeStr)

        redirectAttributes.addFlashAttribute("insulinData", insulinData)
        redirectAttributes.addFlashAttribute("insulinTime", insulinTimeStr)

        return "redirect:/"

    }
}