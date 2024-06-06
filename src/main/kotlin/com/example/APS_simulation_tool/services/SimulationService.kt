package com.example.APS_simulation_tool.services

import com.example.APS_simulation_tool.models.SimulationSettings
import com.example.APS_simulation_tool.repositories.SimulationSettingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * This Service implements the CRUD-interactions between the Repository and the Controller
 *
 */

@Service
class SimulationService(@Autowired var simulationSettingRepo: SimulationSettingRepository)
{

    fun deleteLine(simSetting:SimulationSettings){
        this.simulationSettingRepo.delete(simSetting)
    }

    fun getDefault():SimulationSettings{
        return SimulationSettings(false)

    }
    fun getall():Iterable<SimulationSettings>{
        return simulationSettingRepo.findAll()
    }

    fun getLineById(id: Long): SimulationSettings {
        if (this.simulationSettingRepo.existsById(id)){
            return this.simulationSettingRepo.findById(id).get()
        }else{
            return this.getDefault()
        }
    }

    /**
     * This function saves a new line in the sim_settings db
     */
    fun saveLine(s:SimulationSettings):SimulationSettings{
        var sim = SimulationSettings(s.readyToPlot, s.algorithm, s.sensor, s.insulinPump, s.virtualPatient, s.meals)
        return this.simulationSettingRepo.save(s)
    }

    /**
     * This Function keeps the ID of the old item when updating
     */
    fun saveExistingLine(s:SimulationSettings):SimulationSettings{
        return this.simulationSettingRepo.save(s)
    }

    fun getAll(): Iterable<SimulationSettings>{
    return this.simulationSettingRepo.findAll()
    }

    fun deleteLineById(id: Long) {
        return this.simulationSettingRepo.deleteById(id)

    }

    fun updateItem(item: SimulationSettings):SimulationSettings {
        this.deleteLineById(item.getId())
        return this.saveExistingLine(item)

    }


}