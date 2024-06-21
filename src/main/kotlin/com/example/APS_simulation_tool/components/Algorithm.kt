package com.example.APS_simulation_tool.components

import java.time.*

class Algorithm {
    fun getInsulinFromAlgorithm(time: LocalTime, incrementer: Int): Double { //return amount insulin
        var insulin = 0.0
        if (incrementer.mod(5) == 0) { // every 5 minutes check for insulin to come
            insulin = 3.0
        }
        return insulin
    }
}
