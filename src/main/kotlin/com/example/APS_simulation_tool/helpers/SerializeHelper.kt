package com.example.APS_simulation_tool.helpers

import com.example.APS_simulation_tool.models.MealParameter
import com.example.APS_simulation_tool.models.Parameter
import com.example.APS_simulation_tool.models.TimeParameter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object SerializeHelper {
    private val objectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        registerKotlinModule()
    }

    /**
     * This function takes a List<Parameter(String,Number,"Double"|"Integer")> and returns its JSON string representation
     * If an error occurs during serialization, it returns null (or you could handle it differently based on your requirements).
     */
    fun serializeListOfMealParameter(listOfParameter: List<MealParameter>?): String {
        return try {
            objectMapper.writeValueAsString(listOfParameter) // test Null case
        } catch (e: JsonProcessingException) {
            // Log error or handle it as needed
            ""
        }
    }


    /**
     * This function takes a List<Parameter(String,Number,"Double"|"Integer")> and returns its JSON string representation
     * If an error occurs during serialization, it returns null (or you could handle it differently based on your requirements).
     */
    fun serializeListOfParameter(listOfParameter: List<Parameter>?): String {
        return try {
            objectMapper.writeValueAsString(listOfParameter) // test Null case
        } catch (e: JsonProcessingException) {
            // Log error or handle it as needed
            ""
        }
    }

    /**
     * This function takes a List<Parameter(String,Number,"Double"|"Integer")> and returns its JSON string representation
     * If an error occurs during serialization, it returns null (or you could handle it differently based on your requirements).
     */
    fun serializeListOfTimeParameter(listOfParameter: List<TimeParameter>?): String {
        return try {
            objectMapper.writeValueAsString(listOfParameter) // test Null case
        } catch (e: JsonProcessingException) {
            // Log error or handle it as needed
            ""
        }
    }

    /**
     * This function takes a JSON string and attempts to deserialize it into a List<Pair<String, Int>>.
     * It uses Jackson's TypeReference to work around type erasure and ensure the generic types are correctly understood during deserialization. Similar to serialization, it returns null in case of an error.
     */
    fun deserializeToListOfParameter(json: String): List<Parameter>? {
        return try {
            objectMapper.readValue(json, object : TypeReference<List<Parameter>>() {}) // write test for Null case
        } catch (e: JsonProcessingException) {
            // Log error or handle it as needed Better return something that tells user that the values are not valid
            throw e
            // implement Message to be logged before throw
        }
    }

    /**
     * This function takes a JSON string and attempts to deserialize it into a List<Pair<String, Int>>.
     * It uses Jackson's TypeReference to work around type erasure and ensure the generic types are correctly understood during deserialization. Similar to serialization, it returns null in case of an error.
     */
    fun deserializeToListOfMealParameter(json: String): List<MealParameter>? {
        return try {
            objectMapper.readValue(json, object : TypeReference<List<MealParameter>>() {}) // write test for Null case
        } catch (e: JsonProcessingException) {
            // Log error or handle it as needed Better return something that tells user that the values are not valid
            throw e
            // implement Message to be logged before throw
        }
    }

    /**
     * This function takes a JSON string and attempts to deserialize it into a List<Pair<String, Int>>.
     * It uses Jackson's TypeReference to work around type erasure and ensure the generic types are correctly understood during deserialization. Similar to serialization, it returns null in case of an error.
     */
    fun deserializeToListOfTimeParameter(json: String): List<TimeParameter>? {
        return try {
            objectMapper.readValue(json, object : TypeReference<List<TimeParameter>>() {}) // write test for Null case
        } catch (e: JsonProcessingException) {
            // Log error or handle it as needed Better return something that tells user that the values are not valid
            throw e
            // implement Message to be logged before throw
        }
    }


    /*
    The TypeReference is used here to preserve the type information that is lost due to type erasure in Java generics,
    To deserialize the JSON string correctly into List<Pair<String, Int>>
    */

}

// Usage example
// val myList = listOf(Pair("First", 1), Pair("Second", 2))
// val json = SerializationHelper.serializeToJson(myList)
// val deserializedList: List<Pair<String, Int>>? = SerializationHelper.deserializeFromJson(json!!)
