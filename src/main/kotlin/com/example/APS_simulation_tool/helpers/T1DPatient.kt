package com.example.APS_simulation_tool.helpers

import com.example.APS_simulation_tool.models.MealParameter
import com.example.APS_simulation_tool.models.Parameter
import com.example.APS_simulation_tool.models.TimeParameter
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import org.apache.commons.math3.ode.FirstOrderIntegrator
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator
import org.apache.commons.math3.ode.sampling.StepHandler
import org.apache.commons.math3.ode.sampling.StepInterpolator

import java.time.*
import kotlin.random.Random


data class ODESolutions(
        val times: MutableList<Double> = mutableListOf(),
        val ys: List<MutableList<Double>> = List(13) { mutableListOf() } // here comes the 13xN-matrix with the states
)

data class Action(var CHO: Double, var insulin: Double)

/**
 * This class holds all the Patient-relevant Parts and must be initialised with the Parameters from the DB in Map<String,Double> form.
 * the initial Conditions give the state of the Patient at the Beginning of the Simulation
 */
class T1DPatient(

        var parameters: MutableMap<String,Double>,
        var initialConditions:DoubleArray , // Initial conditions for all state components
        var currentConditions: DoubleArray = doubleArrayOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0),
        val solutions:ODESolutions = ODESolutions(),
        var ode: UVAPadovaODE = UVAPadovaODE(parameters), //innit with wrong parameters,
        var stepHandler: SimpleStepHandler = SimpleStepHandler(),
        var integrator: FirstOrderIntegrator = DormandPrince853Integrator(1e-7, 1.0, 1e-7, 1e-7),
        var startTime: LocalTime,
        var tStart:Long = 0,
        var tEnd:Long = 1,
        var carbsStorage: Double = 0.0
) {

    init {
        //reset()
        integrator.addStepHandler(this.stepHandler) // adding stephandler
        currentConditions = initialConditions

    }
    fun patientEats(eatenCarbs: Int){
        this.carbsStorage -= eatenCarbs
    }
    fun prepareNextMeal():Double{
        if (carbsStorage >=0.0){ //is patient still eating?
            if (this.carbsStorage > 0.0){
                val nextMeal = this.carbsStorage - EATING_RATE
                //check if Meal is empty or there is more to come
                if (nextMeal < 0){ //less than 5grams in storage

                    return carbsStorage
                }else{
                    return EATING_RATE // 5grams/min
                }
            }
        }else{
            //println("maybe something went wrong - not eating")
            return 0.0 // not eating - return 0
        }

        return 0.0
    }

    fun setParams(params: Map<String,Double>){
        this.ode.updateParams(params)
    }
    fun updateODEParameters(paramsMap: MutableMap<String, Double>){
        this.parameters = paramsMap // new parameters
        ode= UVAPadovaODE(parameters) // overwrite ODE with new ODE with updated parameters
    }
    // This function takes one step into the future by integrating over 1min  (timeperiod)
    // and returning the (BG,Time) Pair after that minute
    // it also decreases the carbsStorage by the Eating rate if the patient has eaten something

    fun step(action: Action):Pair<Double,LocalTime> {
        // Update parameters based on the action
        this.parameters.put("CHO",action.CHO)// in grams
        this.parameters.put("insulin", action.insulin)
        // Re-setup the solver with updated parameters
        this.setParams(this.parameters)

        integrator.integrate(ode, tStart.toDouble(), currentConditions, tStart + Companion.SAMPLE_TIME, currentConditions)
        // now reduce the amount of carbs that already influenced the ODE
        this.patientEats(this.parameters.getValue("CHO").toInt())


        // THE STEP HANDLER SEEMS TO SHOW THE INTERNAL STATES THAT WHERE USED FOR CALCULATIONS
        // GET TO THe WHISHED ENDSTATE AT TIME = +1min
        //println("Here (after integration) CURRENTCONDITIONS we have the state:")
        //println(this.currentConditions[12])
        // INTIIAL CONDITIONS BEING ALTERED - MAKES NO SENSE:
        //increase the Time Step
        this.tStart += SAMPLE_TIME.toLong()
        this.tEnd += SAMPLE_TIME.toLong()


        return Pair(observation(this.currentConditions[12]),startTime.plusMinutes(tEnd))
    }

    fun reset() {

        this.parameters.put("CHO",0.0)// in grams
        this.parameters.put("insulin", 0.0)


    }

    fun observation(currentBG:Double):Double{
        val Gsub = currentBG / parameters.getValue("Vg")
        return Gsub
    }

    companion object {
        const val SAMPLE_TIME = 1.0 // min
        const val EATING_RATE = 5.0 // g/min CHO

    }
}

data class SimulationParameters(
        val parameters: Map<String, Double>,
        var state: DoubleArray
)

class PatientHelper {
    fun createInitialConditionsForPatient():DoubleArray{
        val ys = doubleArrayOf(0.0,0.0,0.0,247.621836,176.506559902,4.697517762,0.0,97.554,97.554,3.19814917262,57.951224472,
                93.2258828462, 250.621836)
        return ys
    }
    fun createMealsMapFromMealParameterList(parameterList: List<MealParameter>):MutableMap<LocalTime, Double>{
        // empty Hashmap
        val mealsMap = mutableMapOf<LocalTime, Double>()
        for (p in parameterList){
            mealsMap[p.startTime] = p.carbs.toDouble() // should be Double anyways For Patient Parameters
        }
        return mealsMap

    }
    fun createHashMapFromPatientParameterList(parameterList: List<Parameter>):MutableMap<String,Double>{
        // empty Hashmap
        val paramsMap = mutableMapOf<String, Double>()
        for (p in parameterList){
                paramsMap[p.name] = p.value.toDouble() // should be Double anyways For Patient Parameters
        }
    return paramsMap

    }
    fun createHashMapFromTimeParameterList(parameterList: List<TimeParameter>):MutableMap<String,LocalTime>{
        // empty Hashmap
        val paramsMap = mutableMapOf<String, LocalTime>()
        for (p in parameterList){
            paramsMap[p.name] = p.clockTime // should be Double anyways For Patient Parameters
        }
        return paramsMap

    }
    fun createDefaultHashmap():Map<String,Double>{
        // BUILDING HASHMAP OF PARAMETERS:
        val csvHeaders = "BW,EGPb,Gb,Ib,kabs,kmax,kmin,b,d,Vg,Vi,Ipb,Vmx,Km0,k2,k1,p2u,m1,m5,CL,HEb,m2,m4,m30,Ilb,ki,kp2,kp3,f,Gpb,ke1,ke2,Fsnc,Gtb,Vm0,Rdb,PCRb,kd,ksc,ka1,ka2,dosekempt,u2ss,isc1ss,isc2ss,kp1,patient_history"
        val csvValues = "88.706,3.3924,149.02,97.554,0.091043,0.015865,0.0083338,0.83072,0.32294,1.6818,0.048153,4.697517762,0.074667,260.89,0.067738,0.057252,0.021344,0.15221,0.029902,0.8571,0.6,0.259067825939,0.103627130376,0.228315,3.19814917262,0.0088838,0.023318,0.023253,0.9,250.621836,0.0005,339,1,176.506559902,5.92854753098,3.3924,0.0227647295665,0.0185,0.056,0.0025,0.0115,90000,1.21697571391,57.951224472,93.2258828462,11.5048231338,0"

        val headers = csvHeaders.split(',')
        val values = csvValues.split(',')
        val paramsMap = mutableMapOf<String, Double>()
        if (headers.size == values.size) {
            headers.zip(values).forEach { (header, value) ->
                try {
                    // Try to convert the value to Double, otherwise keep as String
                    val doubleValue = value.toDouble()
                    paramsMap[header] = doubleValue
                } catch (e: NumberFormatException) {
                    // If it is not a number, store the value 0.1
                    paramsMap[header] = 0.1
                }
            }

            // Example usage: print all key-value pairs
            println("DONE: The Hashmap has been successfully created")
        } else {
            println("Error: The number of headers and values does not match.")
        }
        return paramsMap

    }
}

/**
 * This Class holds the UVA/Padova Patient Model which basically consists of a 13 dimensional ODE for calculation of BG values
 * in dynamic response to given Actions (insulin input and carbs input) and
 */
class UVAPadovaODE(private var params: Map<String, Double>) : FirstOrderDifferentialEquations {
        override fun getDimension(): Int = 13

        override fun computeDerivatives(t: Double, y: DoubleArray, yDot: DoubleArray) {
            // Implement your ODE here using the parameters
            // This part just makes sure all values return something usefull
            // to let the ode solver work in case there is a value missing which shouldn't
            // Unpack needed parameters
            val BW = params["BW"] ?: 75.0  // Body weight in kg, defaulting to 75 kg
            val u2ss = params["u2ss"] ?: 0.01
            val kmax = params["kmax"] ?: 0.05
            val kmin = params["kmin"] ?: 0.01
            val kabs = params["kabs"] ?: 0.02
            val f = params["f"] ?: 0.9
            val p2u = params["p2u"] ?: 0.009
            val Ib = params["Ib"] ?: 0.0877
            val kp1 = params["kp1"] ?: 0.03
            val kp2 = params["kp2"] ?: 0.02
            val kp3 = params["kp3"] ?: 0.01
            val ki = params["ki"] ?: 0.01

            val k1 = params["k1"] ?: 0.01
            val k2 = params["k2"] ?: 0.01

            val ksc = params["ksc"] ?: 0.1
            val m1 = params["m1"] ?: 0.01
            val m2 = params["m2"] ?: 0.01
            val m4 = params["m4"] ?: 0.01
            val m30 = params["m30"] ?: 0.01

            val ka1 = params["ka1"] ?: 0.01
            val ka2 = params["ka2"] ?: 0.01
            val ke1 = params["ke1"] ?: 0.01
            val ke2 = params["ke2"] ?: 0.01
            val kd = params["kd"] ?: 0.01

            val fsnc = params["Fsnc"] ?: 0.01
            val d = params["d"] ?: 0.01
            val b = params["b"] ?: 0.01



            val actionCHO = params["CHO"] ?: 0.0  // Carbohydrate intake
            val actionInsulin = 0.1 //params["insulin"] ?: 0.1  // Insulin administration

            // compute and extract some necessary infos from the parameters
            val insulin = (actionInsulin *6000) / BW  //non null
            //var rateOfAppereance = f * kabs * y[2] / BW
            // Variables in Python translated to kotlin
            val lastfoodtaken = 0.0
            val qsto = y[0] + y[1]  // total glucose in the stomach
            val Dbar = qsto + lastfoodtaken * 1000  // converting to mg
            var kgut = 0.0 //innit
            if (Dbar > 0){

                var aa = 5 / (2 * Dbar * (1 - b))
                var cc = 5 / (2 * Dbar * d)
                kgut = kmin + (kmax - kmin) / 2 * (
                        kotlin.math.tanh(aa * (qsto - b * Dbar)) -
                                kotlin.math.tanh(cc * (qsto - d * Dbar)) + 2)
            }
            else{
                kgut = kmax
            }
            //---------- Table for the ODE solver ordering--------------------------------------------------------------
            //|-----|-----|-----|-----|-----|------|-----||-----|-----||-!!!-|
            //|Gp(t)|Gt(t)|Ip(t)|X(t) |I'(t)|X^l(t)|Il(t)||Isc1 |Isc2 ||Gs(t)|
            //| y[3]| y[4]| y[5]|y[6] | y[7]| y[8] | y[9]||y[10]|y[11]||y[12]|
            //|_____|_____|_____|_____|_____|______|_____||_____|_____||_____|

            // Stomach solid phase dynamics
            yDot[0] = -kmax * y[0] + actionCHO * 1000  // CHO converted to mg
            // Stomach liquid phase dynamics
            yDot[1] = kmax * y[0] - y[1] * kgut
            // Intestine dynamics
            yDot[2] = kgut *y[1]- kabs * y[2]
            //Rate of appearance
            var  Rat = f * kabs * y[2] / BW
            //Glucose Production
            var  EGPt = kp1 - kp2 * y[3] - kp3 * y[8]
            //Glucose Utilization
            var  Uiit = fsnc
            // renal excretion (de:Niere)
            var Et = 0.0 // innit
            if (y[3] > ke2){
                Et = ke1 * (y[3] - ke2)}
            else{
                Et = 0.0}
            //-------------------------------------Glucose Subsystem--------------------------------------------------
            //BG kinetics pt.I:
            yDot[3] = Math.max(EGPt, 0.0) + Rat - Uiit - Et - k1 * y[3] + k2 * y[4]
            //if( yDot[3]< 0) yDot[3] = 0.0

            // Glucose kinetics pt. II
            var Vmt = params.getValue("Vm0") + params.getValue("Vmx") * y[6]
            var Kmt = params.getValue("Km0")

            var Uidt = Vmt * y[4] / (Kmt + y[4])

            yDot[4] = -Uidt + k1 * y[3] - k2*y[4]
            if (yDot[4]<0) yDot[4]=0.0

            // Insulin Kinetics
            yDot[5] = -( m2 + m4 ) * y[5] + m1 * y[9] + (ka1 * y[10] + ka2 * y[11]) // ....+ ( Rai(t) )

            var It = y[5] / params.getValue("Vi")
            if(y[5] < 0) yDot[5]=0.0

            // insulin action on glucose production X(t) flucose utilization
            yDot[6] = -p2u*y[6]+p2u*(It-Ib)

            // insulin action on production I'(t)
            yDot[7] = ki *(y[7] - It)
            //X^l(t)
            yDot[8] = ki * (y[8] - y[7])

            //
            // insulin in the liver (pmol/kg)
            yDot[9] = -(m1 + m30) * y[9] + m2 * y[5]
            if (y[9] < 0) yDot[9] = 0.0  // Ensuring y[9] does not go negative

            // Subcutaneous insulin kinetics
            yDot[10] = insulin - (ka1 + kd) * y[10]
            if (y[10] < 0) yDot[10] = 0.0  // Ensuring y[10] does not go negative

            yDot[11] = kd * y[10] - ka2 * y[11]
            if (y[11] < 0) yDot[11] = 0.0  // Ensuring y[11] does not go negative

            // Subcutaneous glucose
            yDot[12] = (-ksc * y[12] + ksc * y[3])
            if (y[12] < 0) yDot[12] = 0.0  // Ensuring y[12] does not go negative
        }

        fun updateParams(newParams: Map<String, Double>) {
            params = newParams
        }
    }

/**
 * The SimplestepHandler gets added to the (imported) Integrator. He tells the integrator how to Handle the
 * Data through each integration step. We mainly just want to store the value after each step (where 1 Step is
 * equivalent of making a time progress of 1 min)!
 */
    class SimpleStepHandler : StepHandler {
        val times: MutableList<Double> = mutableListOf()
        val states: MutableList<DoubleArray> = mutableListOf()

        override fun init(t0: Double, y0: DoubleArray, t: Double) {
            times.add(t0)
            states.add(y0.clone())
        }
        // Handle each step
        override fun handleStep(interpolator: StepInterpolator, isLast: Boolean) {
            val t = interpolator.currentTime
            val y = interpolator.getInterpolatedState()
            times.add(t)
            states.add(y.clone())

            if (isLast) {
                println("Final state at t=$t: ${y.joinToString()}")
            }
        }
    }

    fun runSimulation(initialParams: Map<String, Double>, initialConditions: DoubleArray):DoubleArray {
        val integrator: FirstOrderIntegrator = DormandPrince853Integrator(1e-7, 1.0, 1e-7, 1e-7)
        val ode = UVAPadovaODE(initialParams)
        val stepHandler = SimpleStepHandler()

        integrator.addStepHandler(stepHandler)
        integrator.integrate(ode, 0.0, initialConditions, 1.0, DoubleArray(ode.dimension))

        // Access results from the step handler
        stepHandler.states.forEachIndexed { index, state ->
            println("Time: ${stepHandler.times[index]}, State: ${state.joinToString()}")
        }
        return stepHandler.states.last()
    }

