package com.example.APS_simulation_tool.helpers

import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader

/**
 * This class parses the CSV file into the right object but since it was not needed
 * at the end I might just delete the class.
 */

class ReadWrite {


    @Throws(IOException::class)
    fun parseCSV(@RequestParam file: MultipartFile): String {
        val reader: Reader = InputStreamReader(file.inputStream)

        // Parse CSV data
        val csvReader: CSVReader = CSVReaderBuilder(reader).build()
        val rows: List<Array<String>> = csvReader.readAll()

        // Analyze data...

        return "Processed " + rows.size + " rows!"
    }



}