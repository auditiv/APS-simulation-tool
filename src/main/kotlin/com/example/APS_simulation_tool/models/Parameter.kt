package com.example.APS_simulation_tool.models

import com.fasterxml.jackson.annotation.*
import java.time.LocalTime


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = Parameter::class, name = "Double"),
        JsonSubTypes.Type(value = Parameter::class, name = "Integer"),
        JsonSubTypes.Type(value = TimeParameter::class, name = "Time"),
        JsonSubTypes.Type(value = MealParameter::class, name = "Meal")
)

//@JsonIgnoreProperties(value = ["clockTime"])
open class Parameter(
        var name: String = "",
        var value: Number = -1,
        @JsonProperty("type")
        var type: String = "",
        //var ptype: String = "",
        var unit : String = "",
        var description: String = ""
) {
    init {
        setType()
    }

    private fun setType() {
        if (value.toDouble() > 0) {
            when (this.value) {

                is Double -> type = "Double"  // Assigning Double
                is Int -> type = "Integer"   // Assigning Integer
                // Assigning Integer
            }
        }else{
            if (type != "Meal")
            type = "Time"
            else{
                type = "Meal"
            }
        }


    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Parameter

        if (name != other.name || value != other.value || type != other.type || unit != other.unit || description != other.description)
            return false // not equal

        return true // else : equal!

    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + unit.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}

/**
 * This class describes the Model for the time Parameter of the Simulation
 * Using  ISO 8601 format
 */
@JsonTypeName("TimeParameter")
class TimeParameter(
        name: String = "",
        description: String = "",
        @JsonProperty("clockTime")
        var clockTime: LocalTime = LocalTime.of(7,0)
) : Parameter(name = name, description = description, type = "Time", unit = "HH:MM", value = -1){
    init {
        setType()
    }
    fun setType(){
        if(clockTime != null){
            type = "Time"
        }

    }
}

@JsonTypeName("MealParameter")
class MealParameter(
        name: String = "",
        description: String = "",
        var unit2: String = "grams of carbs",
        @JsonProperty("startTime")
        var startTime: LocalTime = LocalTime.of(7,0),
        var carbs: Int = 50
) : Parameter(name = name, description = description, type = "Meal", unit = "HH:MM", value = -1){

}