package com.example.APS_simulation_tool.services

import java.time.LocalTime
import com.example.APS_simulation_tool.helpers.SerializeHelper
import com.example.APS_simulation_tool.models.*
import com.example.APS_simulation_tool.repositories.ParametersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * This Class implements the interface between the Controller and the repositories (DB)
 * for the parameters of each component.
 * Either in list<Parameter> of in JSON-Str format
 */
@Service
class ParametersService(@Autowired var settingParametersRepo: ParametersRepository) {

    fun createParametersFromParametersView(paramView: ParametersView):ParametersTable{
        return ParametersTable(SerializeHelper.serializeListOfParameter(paramView.virtualPatientParams),
                SerializeHelper.serializeListOfParameter(paramView.algorithmParams),
                SerializeHelper.serializeListOfParameter(paramView.sensorParams),
                SerializeHelper.serializeListOfParameter(paramView.insulinPumpParams),
                SerializeHelper.serializeListOfParameter(paramView.mealsParams),
                SerializeHelper.serializeListOfParameter(paramView.generalParams),
                paramView.id
                )
    }

    /**
     * Here we should load from a CSV with default values for an Adult
     */
    fun createDefaultParameters(settings: ComponentsTable): ParametersTable {
        // create parameters for UVA/Padova:
        var parametersUvaPadova = emptyList<Parameter>()
                if (settings.virtualPatient == "UVA/Padova") {
                    parametersUvaPadova = listOf(

                                Parameter("BW", 88.706, "Double", "kg", "Mass of individual"),
                                Parameter("EGPb", 3.3924, "Double", "μmol/kg/min", "Baseline rate of glucose production by the liver"),
                                Parameter("Gb", 149.02, "Double", "mg/dL", "Baseline concentration of glucose in the blood"),

                                Parameter("Ib", 97.554, "Double", "μU/mL", "Baseline concentration of insulin in the blood"),
                                Parameter("kabs", 0.091043, "Double", "1/min", "Rate at which a substance is absorbed into the bloodstream"),
                                Parameter("kmax", 0.015865, "Double", "1/min", "Maximum rate of process under specific conditions"),
                                Parameter("kmin", 0.0083338, "Double", "1/min", "Minimum rate of process under specific conditions"),

                                Parameter("b", 0.83072, "Double", "Unitless", "Coefficient representing insulin action on glucose utilization"),
                                Parameter("d", 0.32294, "Double", "Unitless", "Coefficient representing the rate of insulin degradation"),
                                Parameter("Vg", 1.6818, "Double", "L", "Volume in which glucose is distributed in the body"),
                                Parameter("Vi", 0.048153, "Double", "L", "Volume in which insulin is distributed in the body"),

                                Parameter("Ipb", 4.697517762, "Double", "μU/mL/min", "Baseline insulin production rate"),
                                Parameter("Vmx", 0.074667, "Double", "mg/dL/min", "Maximum rate at which insulin can act to lower blood glucose"),
                                Parameter("Km0", 260.89, "Double", "mg/dL", "Constant describing insulin sensitivity in glucose utilization"),
                                Parameter("k2", 0.067738, "Double", "1/min", "Describes the rate of second phase insulin action"),

                                Parameter("k1", 0.057252, "Double", "1/min", "Describes the rate of first phase insulin action"),
                                Parameter("p2u", 0.021344, "Double", "Unitless", "Parameter describing the effect of insulin on glucose utilization"),
                                Parameter("m1", 0.15221, "Double", "1/min", "rate parameter from Il to Ip in 1/min"),
                                Parameter("m5", 0.029902, "Double", "Unitless", "Model constant"),

                                Parameter("CL", 0.8571, "Double", "L/min", "Rate at which a substance is cleared from the body"),
                                Parameter("HEb", 0.6, "Double", "Unitless", "Baseline hepatic extraction rate of a substance"),

                                Parameter("m2", 0.259067825939, "Double","1/min", "rate parameter from Ip to Il "),
                                Parameter("m4", 0.103627130376, "Double", "1/min", "rate parameter from Ip to periphery"),
                                Parameter("m30", 0.228315, "Double", "1/min", "rate parameter from Ip to periphery"),

                                Parameter("Ilb", 3.19814917262, "Double", "μU/mL", "Baseline insulin concentration in the liver"),
                                Parameter("ki", 0.0088838, "Double", "1/min", "Rate constant for insulin's effect on liver glucose production"),

                                Parameter("kp2", 0.023318, "Double", "1/min", "Rate constants describing glucagon's effect on glucose production"),
                                Parameter("kp3", 0.023253, "Double", "1/min", "Rate constants describing glucagon's effect on glucose production"),
                                Parameter("f", 0.9, "Double", "Unitless", "Function describing modifications in glucagon action based on other factors"),
                                Parameter("Gpb", 250.621836, "Double", "mg/dL", "Basal concentration of glucose in the plasma"),

                                Parameter("ke1",0.0005, "Double", "1/min", "Rate constant for glucose or insulin excretion"),
                                Parameter("ke2", 339, "Double", "1/min", "Rate constant for glucose or insulin excretion"),
                                Parameter("Fsnc", 1, "Double", "mg/kg/min", "Rate of glucose uptake by the brain"),
                                Parameter("Gtb", 176.506559902, "Double", "mg/dL", "Basal glucose level below which certain physiological responses are triggered"),

                                Parameter("Vm0", 5.92854753098, "Double", "mg/dL/min", "Basal rate at which insulin can act to lower blood glucose"),
                                Parameter("Rdb", 3.3924, "Double", "U/min", "Baseline rate at which drugs (like insulin) are absorbed into the bloodstream"),

                                Parameter("PCRb", 0.0227647295665, "Double", "g/kg/day", "Baseline rate of protein breakdown in the body"),
                                Parameter("kd", 0.0185, "Double", "1/min", "Rate constant for the degradation of a substance"),

                                Parameter("ksc",0.056, "Double", "1/min", "Subcutaneous absorption rate constant"),
                                Parameter("ka1", 0.0025, "Double", "1/min", "Absorption rate constant for a drug"),
                                Parameter("ka2", 0.0115, "Double", "1/min", "Absorption rate constant for a drug"),
                                Parameter("dosekempt", 90000, "Double", "Unitless", "Dosing coefficient"),
                                Parameter("u2ss",1.21697571391, "Double", "Unitless", "Steady state urinary excretion rate"),
                                Parameter("isc1ss", 57.951224472, "Double", "Unitless", "First steady state subcutaneous insulin concentration"),
                                Parameter("isc2ss", 93.2258828462, "Double", "Unitless", "Second steady state subcutaneous insulin concentration"),
                                Parameter("kp1", 11.5048231338, "Double", "1/min", "Rate constant in glucagon kinetics"),
                                Parameter("patient_history",0, "Double", "Unitless", "Parameter derived from simulation history"),

                    )
        }


        // create parameters for algorithm basic basal or oref0
        var parametersAlgorithm = emptyList<Parameter>()
        if (settings.algorithm == "BasicBasal") {
            parametersAlgorithm = listOf(Parameter("basal insulin", 1.509, "Double", "mg", "Basal Insulin Dose"))
        }
        if (settings.algorithm == "oref0"){
            parametersAlgorithm = listOf(
                    Parameter("current_basal", 0.7, "Double", "U/h", description = "default basal rate"),
                    Parameter("sens", 50.0, "Double", "(mg/dl)/U", description =  " insulin sensitivity"),
                    Parameter("dia", 6.0, "Double", "h", description = "duration of insulin action"),
                    Parameter("carb_ratio", 8.0, "Double", "g/U", description = "carbohydrate ratio"),
                    Parameter("max_iob", 3.5, "Double", "U", description = "maximum insulin on board"),
                    Parameter("max_basal", 3.5, "Double", "U/h", description = "maximum basal rate"),
                    Parameter("max_daily_basal", 2.0, "Double", "U", description = "maximum daily basal"),
                    Parameter("max_bg", 120.0, "Double", "mg/dl", description = "maximum blood glucose"),
                    Parameter("min_bg", 120.0, "Double", "mg/dl", description = "minimum blood glucose"),
                    Parameter("maxCOB", 120.0, "Double", "g", description = "maximum carbohydrates on board")
            )

        }
        // create parameters for IDEAL Sensor:
        var parametersSensor = emptyList<Parameter>()
        if (settings.sensor== "Ideal-CGM-Sensor") {
            parametersSensor = listOf(Parameter("samplingtime", 5, "Integer", "min", "CGM sampling time interval in minutes"),
                    Parameter("error_range", 3.5, "Double", "error, double", "Error Range of each measurement"))
        }
        // create parameters for Insulin Pump
        var parametersInsulinPump = emptyList<Parameter>()
        if (settings.insulinPump== "Ideal-Insulin-Pump"){
            parametersInsulinPump = listOf(
                    Parameter(name = "abserr_basal", value = 0.5, type = "Double", unit = "",  description = "injection error, must be greater than zero"),
                    )}
        // create parameters for Meals
        var parametersMeals = emptyList<Parameter>()
        if (settings.meals == "1") {
            parametersMeals = listOf(MealParameter("Breakfast", "First Meal Time of the Day in carbs", "carbs in grams", LocalTime.of(8, 0), carbs = 50),
            )}
        if (settings.meals == "2") {
            parametersMeals = listOf(MealParameter("Breakfast", "First Meal Time of the Day in carbs", "carbs in grams", LocalTime.of(8, 0), carbs = 50),
                    MealParameter("Lunch", "Main Meal Time of the Day in carbs", "carbs in grams", LocalTime.of(12, 0), carbs = 50),
            )}
        if (settings.meals == "3") {
            parametersMeals = listOf(MealParameter("Breakfast", "First Meal Time of the Day in carbs", "carbs in grams", LocalTime.of(8, 0), carbs = 50),
                    MealParameter("Lunch", "Main Meal Time of the Day in carbs", "carbs in grams", LocalTime.of(12, 0), carbs = 50),
                    MealParameter("Dinner", "Last Meal Time of the Day in carbs", "carbs in grams", LocalTime.of(18, 0), carbs = 50)
            )}
        // create parameters for General Settings
        // get local time for testing.
        val currentTime = LocalTime.of(8, 0)
        val endTime = LocalTime.of(16, 0)

/*        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val current = currentTime.format(formatter)*/
        val parametersGeneral = listOf(
                TimeParameter("Simulation Start",description = "Starting Time of the Simulation", clockTime = currentTime),
                TimeParameter("Simulation End",description = "Ending Time of Simu", clockTime = endTime))
        // Serialize all data (JSON-Str FORMAT TO STORE IN DB)
        val serialPatient = SerializeHelper.serializeListOfParameter(parametersUvaPadova)
        val serialAlgo = SerializeHelper.serializeListOfParameter(parametersAlgorithm)
        val serialSensor = SerializeHelper.serializeListOfParameter(parametersSensor)
        val serialInsu = SerializeHelper.serializeListOfParameter(parametersInsulinPump)
        val serialMeal = SerializeHelper.serializeListOfParameter(parametersMeals)
        val serialGeneral = SerializeHelper.serializeListOfParameter(parametersGeneral)
        //create

        //Storing AlgorithmParameters as JSON
        var params = ParametersTable(serialPatient, serialAlgo, serialSensor, serialInsu, serialMeal, serialGeneral, settings.getId())
        return this.settingParametersRepo.save(params)
    }

    fun saveParameters(s: ParametersTable, id: Long): ParametersTable {
        var sim = ParametersTable( s.virtualPatientParams, s.algorithmParams, s.sensorParams, s.insulinPumpParams,
                s.mealsParams, s.generalParams, id)
        return this.settingParametersRepo.save(sim)
    }

    fun deleteParameterById(id: Long) {
        this.settingParametersRepo.deleteById(id)

    }

    fun getParametersById(id: Long): ParametersTable {
        return this.settingParametersRepo.findById(id).get()

    }
}

