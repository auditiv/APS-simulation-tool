package com.example.APS_simulation_tool.helpers

import com.example.APS_simulation_tool.models.MealParameter
import com.example.APS_simulation_tool.models.Parameter
import com.example.APS_simulation_tool.models.TimeParameter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalTime

class ParameterSerializeHelperTest{
    @Test
    fun `serialize a listOf(MealParams)`(){
        //When


        val listParams = listOf(
                MealParameter("Meal1", "First Meal Of the Day","carbs in grams", LocalTime.of(8,12), 50)
        )
        //Then
        val SerializedMealParam = SerializeHelper.serializeListOfMealParameter(listParams)
        //Assert
        assertEquals("""[{"name":"Meal1","description":"First Meal Of the Day","unit2":"carbs in grams","startTime":[8,12],"carbs":50,"value":-1,"type":"Meal","unit":"HH:MM"}]""",SerializedMealParam)


    }
    @Test
    fun `serialize a listOf(Parameter)`(){
        //When


        val listParams = listOf(
                Parameter("BGValue", 1.509, "Double", "mg/dl", "Concentration of something in your blood")
        )
        //Then
        val SerializedListParam = SerializeHelper.serializeListOfParameter(listParams)
        //Assert
        assertEquals("""[{"name":"BGValue","value":1.509,"type":"Double","unit":"mg/dl","description":"Concentration of something in your blood"}]""",SerializedListParam)


    }
    @Test
    fun `serialize a listOf(Parameter,TimeParameter)`(){
        //When
        val expectedJSON="""[{"name":"SimTime","description":"Starting Time of the Simulation","clockTime":[8,30],"value":-1,"type":"Time","unit":"HH:MM"},{"name":"BGValue","value":1.509,"type":"Double","unit":"mg/dl","description":"Concentration of something in your blood"}]"""

        val currentTime = LocalTime.of(8, 30)

        var listParams = listOf( TimeParameter("SimTime",description = "Starting Time of the Simulation", clockTime = currentTime),
                Parameter("BGValue", 1.509, "Double", "mg/dl", "Concentration of something in your blood")

        )
        //Then
        val SerializedParamList = SerializeHelper.serializeListOfParameter(listParams)
        //Assert
        assertEquals(expectedJSON,SerializedParamList)


}
@Test
    fun `Deserialize a JSON-String into a listOf(Parameter,TimeParameter)`(){
        //WHEN

        val JSONformatted = """[{"name":"SimTime","description":"Starting Time of the Simulation","clockTime":[8,20],"value":-1,"type":"Time","unit":"HH:MM"},{"name":"BGValue","value":1.509,"type":"Double","unit":"mg/dl","description":"Concentration of something in your blood"},{"name":"inc_basal","value":0.0,"type":"Double","unit":"","description":"empty"}]"""

        val currentTime = LocalTime.of(8,20)

        var expectedList = listOf(TimeParameter("SimTime",description = "Starting Time of the Simulation", clockTime = currentTime),
            Parameter("BGValue", 1.509, "Double", "mg/dl", "Concentration of something in your blood"),
                Parameter(name = "inc_basal", value = 0.0, type = "Double", unit = "",  description = "empty")

    )

    // WHEN
    val deserializedList = SerializeHelper.deserializeToListOfParameter(JSONformatted)

    // THEN

    if (deserializedList != null && deserializedList.size == expectedList.size) {
        deserializedList.forEachIndexed { index, parameter ->
            if (parameter == expectedList[index]) {
                println(parameter.name)
                print(parameter.description)
                print(parameter.type)
                print(parameter.unit)
                println(parameter.value)

                println(expectedList[index].name)
                print(expectedList[index].description)
                print(expectedList[index].type)
                print(expectedList[index].unit)
                println(expectedList[index].value)
            }
        }
    }

    assertEquals(expectedList,deserializedList)


    }
}