package com.example.APS_simulation_tool.helpers

import com.example.APS_simulation_tool.components.PatientHelper
import com.example.APS_simulation_tool.components.runSimulation
import com.example.APS_simulation_tool.models.Parameter
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class T1DPatientKtTest {
    @Test
    fun `Run one simulation step of the patient`() {
        //When
        //Initial Conditions of Patient ys = [x_0, ... ,x_13]
        val ys = doubleArrayOf(0.0,0.0,0.0,247.621836,176.506559902,4.697517762,0.0,97.554,97.554,3.19814917262,57.951224472,
                93.2258828462, 250.621836)
        var PatientParameters = PatientHelper().createDefaultHashmap()
        var expectedState = doubleArrayOf(0.0, 0.0, 0.0, 247.8540650357663, 176.506559902, 4.703836949278609, 9.496631336228257E-4, 97.553601694482, 97.55400089793083, 3.1986572940311455, 63.440328204547825, 93.27663966895531, 250.4649210195185)
        //Then
        var ODEstate = runSimulation(PatientParameters, ys)

        //Assert
        //expected and actual states are similar within a small margin of error
        val delta = 0.0001  // Define tolerance of the result
        assertArrayEquals(expectedState, ODEstate, delta, "The simulation state did not match the expected values within tolerance.")
    }
    @Test
        fun `create HashMap of the patients ParametersList`() {
            //WHEN
            var parametersList = listOf(
                    Parameter("Gpeq", 100.0, "Double", "mg/dl", "blood glucose concentration in homeostasis"),
                    Parameter("BW", 75.0, "Double", "kg", "body weight in kg"),
                    Parameter("CR", 1.0, "Double", "", "carb ratio TODO"))
            var expectedMap: Map<String,Double> = mapOf(parametersList[0].name to parametersList[0].value.toDouble(),
                    parametersList[1].name to parametersList[1].value.toDouble(),
                    parametersList[2].name to parametersList[2].value.toDouble())
            //THEN
            var patientMap = PatientHelper().createHashMapFromPatientParameterList(parametersList)
            //ASSERT
        assertEquals(expectedMap, patientMap, "The maps should be equal")

    }

}
