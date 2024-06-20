package com.example.APS_simulation_tool.models

/**
 * This class was mainly designed to make it easier to get the Data to the Front-End
 * using the thymeleaf extension. it implements a class that holds all the attributes of all objects,
 * necessary for the simulation as List of Parameters to them into the html file.
 * It needs to implements the iterator function so we can iterate over the attributes as if the object of
 * this class was a list itself.
 */


class ParametersView(
        var virtualPatientParams: List<Parameter>? = mutableListOf(),
        var algorithmParams: List<Parameter>? =mutableListOf(),
        var sensorParams: List<Parameter>? =mutableListOf(),
        var insulinPumpParams: List<Parameter>? =mutableListOf(),
        var mealsParams:List<MealParameter>? =mutableListOf(),
        var generalParams: List<TimeParameter>? =mutableListOf(),

        var id: Long = 0L

) : Iterable<List<Parameter>?>
{
    private val parameterList: List<List<Parameter>?>
        get() = listOf(
                virtualPatientParams,
                algorithmParams,
                sensorParams,
                insulinPumpParams,
                mealsParams,
                generalParams,
        )


    fun getParametersId():Long{
        return this.id
    }
    override fun iterator(): Iterator<List<Parameter>?> {
        return parameterList.iterator()
    }

}

