package com.example.APS_simulation_tool.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table

/**
 * This class models the DB of the parameters in a similar way as the ComponentsTable
 * But the type here in each entry is thought to be JSON-String.
 * Each entry recieves its ID by the corresponding ComponentsTable entry (One-to-One relation)
 */
@Entity
@Table(name = "parameters")
class ParametersTable(
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

) : Iterable<String> {
    private val parameterList: List<String>
        get() = listOf(
            virtualPatientParams,
            algorithmParams,
            sensorParams,
            insulinPumpParams,
            mealsParams,
            generalParams,
        )


    fun getParametersId(): Long {
        return this.id
    }

    override fun iterator(): Iterator<String> {
        return parameterList.iterator()
    }

}