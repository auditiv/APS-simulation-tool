package com.example.APS_simulation_tool.controllers

import com.example.APS_simulation_tool.services.ComponentsService
import com.opencsv.CSVReaderBuilder
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader

/**
 * This Controller has the purpose to handle all the datapassing to the index.html view.
 * Which is the loading and returning of the website for the root-url :/
 */
@Controller
class HomeController(@Autowired var simService: ComponentsService) {

    @GetMapping("/") //makes GET REQUEST
    public fun index(redirectAttributes: RedirectAttributes): ModelAndView {
        var modelAndView: ModelAndView = ModelAndView("index")
        modelAndView.addObject("simulations", this.simService.getAll())
        modelAndView.addObject("algorithms", listOf("BasicBasal", "Others"))
        modelAndView.addObject("sensors", listOf("Ideal-CGM-Sensor", "Others"))
        modelAndView.addObject("insulinPumps", listOf("Ideal-Insulin-Pump", "Others"))
        modelAndView.addObject("virtualPatients", listOf("UVA/Padova", "Others"))
        modelAndView.addObject("meals", listOf("1", "2", "3"))
        modelAndView.addObject("sim", this.simService.getDefault())
        modelAndView.addObject("testdata", listOf(1, 2, 3, 4, 5, 6, 7))
        // for the simulation data:
        // Check if redirect attributes contain mockBG and mockTime and add them to the modelAndView
        redirectAttributes.getFlashAttributes().let {
            if (it.containsKey("mockBG")) {
                modelAndView.addObject("mockBG", it["mockBg"])
                modelAndView.addObject("BGUpperData", it["BGUpperData"])
                modelAndView.addObject("BGLowerData", it["BGLowerData"])
            }
            if (it.containsKey("mocktime")) {
                modelAndView.addObject("mocktime", it["mocktime"])
            }
            if (it.containsKey("insulinData")) {
                modelAndView.addObject("insulinData", it["insulinData"])
            }
            if (it.containsKey("insulinTime")) {
                modelAndView.addObject("insulinTime", it["insulinTime"])
                modelAndView.addObject("mealData", it["mealData"])
            }
        }


        return modelAndView
    }
}
