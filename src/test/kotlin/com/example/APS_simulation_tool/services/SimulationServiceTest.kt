package com.example.APS_simulation_tool.services

import com.example.APS_simulation_tool.models.SimulationSettings
import com.example.APS_simulation_tool.repositories.SimulationSettingRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class SimulationServiceTest{
    @Mock
    private lateinit var simulationSettingRepo: SimulationSettingRepository

    @InjectMocks
    private lateinit var simulationService: SimulationService

    @BeforeEach
        fun setUp() {  //checks for each test if a simulationService with its mockRepo ha been constructed
            simulationService = SimulationService(simulationSettingRepo)
        }

    // Test go beneath this:

    @Test
    fun `getDefault returns default settings`() {
        // When
        val result = simulationService.getDefault()

        // Then
        assertFalse(result.isComplete, "togglAdvSettings should be false by default")
        // Assert more properties as needed
        /* assertEquals("expectedValue", result.someOtherProperty, "someOtherProperty should have the expected value")*/
        /* assertTrue(result.anotherProperty, "anotherProperty should be true")*/
    }

    @Test
    fun `getById returns existing SimulationSettings when ID exists`() {
        // Given
        val existId = 1L
        val expectedSimulationSettings = SimulationSettings(false)
        `when`(simulationSettingRepo.existsById(existId)).thenReturn(true)// simulates that the varialbe existId triggers a true a return
        `when`(simulationSettingRepo.findById(existId)).thenReturn(Optional.of(expectedSimulationSettings))
        // simulates that the call by existId gets resolved with Optional wrapper containing the SimulationSetting

        // When
        val result = simulationService.getLineById(existId)
        // Then
        assertNotNull(result)
        assertEquals(false, result.isComplete)
        verify(simulationSettingRepo).existsById(existId)
        verify(simulationSettingRepo).findById(existId)

    }


}