package com.example.APS_simulation_tool.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class GraphController {

    @GetMapping("/displayLineGraph")
    fun barGraph(model: Model): String {
        val surveyMap: MutableMap<String, Int> = LinkedHashMap()
        surveyMap["Python"] = 80
        surveyMap["Go"] = 40
        surveyMap["Rust"] = 20
        surveyMap["C"] = 50
        surveyMap["Kotlin"] = 33
        model.addAttribute("surveyMap", surveyMap)
        return "line-graph"
    }


}