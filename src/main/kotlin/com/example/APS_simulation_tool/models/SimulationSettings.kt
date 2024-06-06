package com.example.APS_simulation_tool.models

import jakarta.persistence.*

/**
 * This class describes the data model of the different component used in a single simulation.
 * (ex: which algorithm was used, which sensor brand/model was taking samples, etc,)
 *
 * vpatient   |  algorithms  |  sensors  |  insulinpumps  |  meal   |  general
 * ---------------------------------------------------------------------------
 *
 *
 */

@Entity
@Table(name = "sim_settings")
class SimulationSettings(

        var readyToPlot: Boolean = false,
        var algorithm: String = "",
        var sensor: String = "",
        var insulinPump: String = "",
        var virtualPatient: String = "",
        var meals: String = ""
        //id: Long

)
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id : Long= 0L

    fun getreadyToPlot():Boolean{
        return this.readyToPlot
    }

    fun setreadyToPlot(newVal:Boolean){
        this.readyToPlot = newVal
    }
    fun getId():Long{
        return this.id
    }

    fun toggle() {
        this.setreadyToPlot(!(this.readyToPlot))
    }

}
