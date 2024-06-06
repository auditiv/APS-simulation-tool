package com.example.APS_simulation_tool.models

/*enum class AlgorithmPart(val str:String){
    ALGORITHM("0ref")
}*/


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

