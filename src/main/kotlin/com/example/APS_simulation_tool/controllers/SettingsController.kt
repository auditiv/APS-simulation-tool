package com.example.APS_simulation_tool.controllers

import com.example.APS_simulation_tool.helpers.SerializeHelper
import com.example.APS_simulation_tool.models.ParametersTable
import com.example.APS_simulation_tool.models.ParametersView
import com.example.APS_simulation_tool.models.ComponentsTable
import com.example.APS_simulation_tool.models.TimeParameter
import com.example.APS_simulation_tool.services.ComponentsService
import com.example.APS_simulation_tool.services.ParametersService

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

/**
 * This controller handles all interactions between GUI (client) and the parameters (ParametersTable)  and
 * components (ComponentsTable) for each instance.
 */
@Controller
class SettingsController(
    @Autowired var compService: ComponentsService,
    //interacts with simulation settings JPA
    @Autowired var paraService: ParametersService
) {
    /**
     * this method updates the values entered into the edit-parameters-page
     * and gives the new values into the DB
     */

    @PostMapping("/update-parameters/{id}")
    fun updateParametersById(
        @PathVariable("id") id: Long,
        @ModelAttribute("simUpdate") paramUpdate: ParametersView,
        result: BindingResult,
        model: Model
    ): String {
        if (result.hasErrors()) {
            // Handle validation errors
            return "errorViewName" // Return to a view that shows errors
        }
        println(
            "THE RECIEVED DATA FROM THE FORM IS:  ${paramUpdate.generalParams?.get(0)?.type}, ${
                paramUpdate.generalParams?.get(
                    1
                )?.type
            } general"
        )
        if (paramUpdate.generalParams?.get(0) is TimeParameter) {
            val timeParam = paramUpdate.generalParams?.get(0) as TimeParameter
            println("The received clockTime is: ${timeParam.clockTime}")
        }
        var temp = paraService.createParametersFromParametersView(paramUpdate)
        println("THE SERIALIZED DATA FROM THE FORM IS:  ${temp.generalParams} general")
        paraService.saveParameters(temp, id)
        return "redirect:/" // Redirect to prevent duplicate submissions or show updated parameters
    }

    /**
     * Deletes the specified entry (by Id) of BOTH Repos, The Simulation settings and the Parameters
     */
    @GetMapping("/delete-simulation/{id}")
    fun delete(@PathVariable("id") id: Long, model: Model): String {
        val simSetting: ComponentsTable = compService.getLineById(id)
        compService.deleteLineById(id) // delete in components Repo
        paraService.deleteParameterById(id) // delete in Parameters Repo
        var url = "redirect:/"
        return url
    }

    /**
     * Redirects to the ParametersForm, ensuring to manipulate the values of the correct entry (chosen by id)
     *
     */
    @GetMapping("/edit-parameters/{id}")
    fun editParameters(@PathVariable("id") id: Long, model: Model): String {

        val parametersTable: ParametersTable = paraService.getParametersById(id)
        // add parameters to ParametersView
        var paramView = ParametersView(
            SerializeHelper.deserializeToListOfParameter(parametersTable.virtualPatientParams),
            SerializeHelper.deserializeToListOfParameter(parametersTable.algorithmParams),
            SerializeHelper.deserializeToListOfParameter(parametersTable.sensorParams),
            SerializeHelper.deserializeToListOfParameter(parametersTable.insulinPumpParams),
            SerializeHelper.deserializeToListOfMealParameter(parametersTable.mealsParams),
            SerializeHelper.deserializeToListOfTimeParameter(parametersTable.generalParams),
            id
        )


        model.addAttribute("paramView", paramView)

        //since the Parameters class takes all attributes as JSON we have to deserialize it
        return "edit-parameters"
    }

    /**
     * Saves the chosen Simulation Settings into the Simulation Repository and
     * creates a default entry with the same id in the Parameters Repo
     */
    @PostMapping("/setting") // in the HTML tag inside the form is referenced inside the <form ...th:object...>
    // each attribute is referenced by the reference <select... name="" >inside the tag
    fun saveSimulationSetting(@Valid sim: ComponentsTable, result: BindingResult, model: Model): String {
        sim.setreadyToPlot(false) // will not be plotted right away
        var settings =
            ComponentsTable(sim.readyToPlot, sim.algorithm, sim.sensor, sim.insulinPump, sim.virtualPatient, sim.meals)
        compService.saveLine(settings)
        paraService.createDefaultParameters(settings) // create default entry of the parameters for the simulation
        var url = "redirect:/"
        return url
    }

    /**
     * Updates the chosen Simulation Settings in the Simulation Repository and
     * toggles the bool readyToPlot in order to select/deselect for plotting purposes
     * It is only poosible to select ONE row for Simulation at a time.
     */
    @PostMapping("/update-feature")
    fun updateFeature(@RequestParam("id") id: Long, @RequestParam("readyToPlot") readyToPlot: Boolean): String {
        // Assuming a service exists that saves the state
        var toBetoggeldEntry = compService.getLineById(id)
        toBetoggeldEntry.toggle()
        compService.setAllEntriesToFalseUnlessOne(toBetoggeldEntry)
        compService.saveExistingLine(toBetoggeldEntry)

        // Redirect to a confirmation page or reload the same page
        return "redirect:/"
    }

}