package com.example.APS_simulation_tool.controllers

import com.example.APS_simulation_tool.helpers.SerializeHelper
import com.example.APS_simulation_tool.models.Parameters
import com.example.APS_simulation_tool.models.ParametersView
import com.example.APS_simulation_tool.models.SimulationSettings
import com.example.APS_simulation_tool.models.TimeParameter
import com.example.APS_simulation_tool.services.SimulationService
import com.example.APS_simulation_tool.services.ParameterService

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*


@Controller
class SettingsController(
        @Autowired var simService:SimulationService,
        //interacts with simulation settings JPA
        @Autowired var paraService:ParameterService
)
{
    /**
     *
     */

    @PostMapping("/update-parameters/{id}")
    fun updateParametersById(@PathVariable("id") id: Long,@ModelAttribute("simUpdate")paramUpdate : ParametersView, result: BindingResult, model: Model): String {
        if (result.hasErrors()) {
            // Handle validation errors
            return "errorViewName" // Return to a view that shows errors
        }
        println("THE RECIEVED DATA FROM THE FORM IS:  ${paramUpdate.generalParams?.get(0)?.type}, ${ paramUpdate.generalParams?.get(1)?.type} general")
        if (paramUpdate.generalParams?.get(0) is TimeParameter) {
            val timeParam = paramUpdate.generalParams?.get(0) as TimeParameter
            println("The received clockTime is: ${timeParam.clockTime}")
        }
        var temp = paraService.createParametersFromParametersView(paramUpdate)
        println("THE SERIALIZED DATA FROM THE FORM IS:  ${temp.generalParams} general")
        paraService.saveParameters(temp, id)
        return "redirect:/" // Redirect to prevent duplicate submissions or show updated parameters
    }
    /*fun updateParametersById(@PathVariable("id") id: Long, @Valid simUpdate: HashMap<String, List<Parameter>?>, result:BindingResult, model: Model):String{
        // create new Parameters Before updateing:
        *//*var temp: Parameters = Parameters(simUpdate.algorithmParams,
                simUpdate.sensorParams,
                simUpdate.insulinPumpParams,
                simUpdate.mealsParams,
                simUpdate.generalParams,
                (simUpdate.virtualPatientParams),
                id)*//*
        var temp = paraService.createDefaultParameters(id)
        paraService.saveParameters(temp, id)

        return "temp.toString()"
    }*/
    /**
     * Deletes the specified entry (by Id) of BOTH Repos, The Simulation settings and the Parameters
     */
    @GetMapping("/delete-simulation/{id}")
    fun delete(@PathVariable("id") id: Long, model: Model): String {
        val simSetting: SimulationSettings = simService.getLineById(id)
        simService.deleteLineById(id) // delete in Simulation Repo
        paraService.deleteParameterById(id) // delete in Simulation Repo
        var url = "redirect:/"
        return url
    }
    /**
     *
     * Redirects to the ParametersForm ensuring to manipulate the values of the right entry by id
     *
     */
    @GetMapping("/edit-parameters/{id}")
    fun editParameters(@PathVariable("id") id:Long, model: Model): String{

        val parameters: Parameters = paraService.getParametersById(id)
        // add parameters to ParametersView
        var paramView = ParametersView(SerializeHelper.deserializeToListOfParameter(parameters.virtualPatientParams),
                SerializeHelper.deserializeToListOfParameter(parameters.algorithmParams),
                SerializeHelper.deserializeToListOfParameter(parameters.sensorParams),
                SerializeHelper.deserializeToListOfParameter(parameters.insulinPumpParams),
                SerializeHelper.deserializeToListOfMealParameter(parameters.mealsParams),
                SerializeHelper.deserializeToListOfTimeParameter(parameters.generalParams),
                id
                )


        model.addAttribute("paramView", paramView)

                //since the PAramters clas takes all attributes as JSON we have to deserialize it
        return "edit-parameters"
    }

    /**
     * Saves the chosen Simulation Settings into the Simulation Repository and
     * creates a default entry with the same id in the Parameters Repo
     */
    @PostMapping("/setting") // in the HTML TAG INSIDE THE  FORM THE OBJECT COMES INTO THE <form ...th:object...> EACH ATTRIBUTE IS REFERENCED BY THE <select... name="" >inside the tag
    fun saveSimulationSetting(@Valid sim: SimulationSettings, result: BindingResult, model: Model):String{ // last is the ID of the whished Simulation to be toggled
        sim.setreadyToPlot(true) // SIMULATION WILL BE PLOTTED
        //var settings = SimulationSettings(sim.readyToPlot, sim.algorithm, sim.sensor, sim.insulinPump, sim.virtualPatient, sim.meals, sim.getId())
        var settings = SimulationSettings(sim.readyToPlot, sim.algorithm, sim.sensor, sim.insulinPump, sim.virtualPatient, sim.meals)//, sim.getId())
        simService.saveLine(settings)
        paraService.createDefaultParameters(settings) // create default entry of the parameters for the simulation
        var url = "redirect:/"
        return url
    }

    /**
     * Updates the chosen Simulation Settings in the Simulation Repository and
     * toggles the bool readyToPlot in order to select/deselect for plotting purposes
     */
    @PostMapping("/update-feature")
    fun updateFeature(@RequestParam("id") id:Long, @RequestParam("readyToPlot") readyToPlot:Boolean):String {
    // Assuming a service exists that saves the state
    var toBetoggeldEntry = simService.getLineById(id)
        toBetoggeldEntry.toggle()
        simService.saveExistingLine(toBetoggeldEntry)

    // Redirect to a confirmation page or reload the same page
    return "redirect:/"
    }



    @GetMapping("/test")
    fun getHome(): String? {
        return "test.html"
    }
    }