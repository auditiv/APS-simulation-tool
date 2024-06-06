package com.example.APS_simulation_tool.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table

/*enum class AlgorithmPart(val str:String){
    ALGORITHM("0ref")
}*/



@Entity
@Table(name = "set_parameters")
class Parameters(
        @Lob // Make storing for long strings possible
        var virtualPatientParams: String,
        @Lob
        var algorithmParams: String,
        var sensorParams: String,
        @Lob
        var insulinPumpParams: String,
        @Lob
        var mealsParams: String,
        @Lob
        var generalParams: String,

        @Id open var id: Long

) : Iterable<String>
{
    private val parameterList: List<String>
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
    override fun iterator(): Iterator<String>  {
        return parameterList.iterator()
    }

}