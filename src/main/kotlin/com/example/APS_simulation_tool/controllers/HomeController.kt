package com.example.APS_simulation_tool.controllers

import com.example.APS_simulation_tool.services.ComponentsService
import com.example.APS_simulation_tool.services.TodoItemService
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


@Controller
class HomeController(@Autowired var todoItemService: TodoItemService, var simService: ComponentsService) {



    @GetMapping("/") //makes GET REQUEST
    public fun index(redirectAttributes: RedirectAttributes): ModelAndView{
        var modelAndView: ModelAndView = ModelAndView("index")
        modelAndView.addObject("todoItems", this.todoItemService.getAll())
        modelAndView.addObject("simulations", this.simService.getAll())
        modelAndView.addObject("algorithms", listOf("BasicBasal","Others"))
        modelAndView.addObject("sensors", listOf("Ideal-CGM-Sensor", "Others"))
        modelAndView.addObject("insulinPumps", listOf("Ideal-Insulin-Pump", "Others"))
        modelAndView.addObject("virtualPatients", listOf("UVA/Padova", "Others"))
        modelAndView.addObject("meals", listOf("1", "2","3"))
        modelAndView.addObject("sim", this.simService.getDefault())
        modelAndView.addObject("testdata", listOf(1,2,3,4,5,6,7))
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

    @PostMapping("/csvFile")
    fun importCSV(@RequestParam csvFile: MultipartFile): String {
        log.info("File Name: " + csvFile.originalFilename)
        log.info("File Size: " + csvFile.size)

        val reader: Reader = InputStreamReader(csvFile.inputStream, "UTF-8")
        var bufferedReader = BufferedReader(reader)
        //bufferedReader.lines() // TODO: something with the buffered lines

        // Parse CSV data
        val csvReader = CSVReaderBuilder(reader).build()
        val rows = csvReader.readAll()

        // Analyze data...
        val allItems = todoItemService.buildToDoFromCSVArray(rows)


        // Analyze data...
        for(item in allItems){
            todoItemService.save(item)
        }
        log.info( "Processed " + allItems[0].toString())
        return "redirect:/"




        // Analyze data...




    }


}