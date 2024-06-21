package com.example.APS_simulation_tool.services

import com.example.APS_simulation_tool.models.ComponentsTable
import com.example.APS_simulation_tool.repositories.ComponentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * This Service implements the CRUD-interactions between the Components Repository (DB) and the Controller
 *
 */

@Service
class ComponentsService(@Autowired var componentsRepo: ComponentsRepository)
{
    /**
     * This function ensures that at most one entry has readyToPlot set to true
     * Takes: Components table entry
     * Updates the Resository (DB)
     */
    fun setAllEntriesToFalseUnlessOne(componentsRow: ComponentsTable){
        for(item in this.componentsRepo.findAll()){
            if (item.getId() != componentsRow.getId()){
                item.setToFalse()
            }
        }
    }

    fun deleteLine(componentsRow:ComponentsTable){
        this.componentsRepo.delete(componentsRow)
    }

    fun getDefault():ComponentsTable{
        return ComponentsTable(false)

    }
    fun getall():Iterable<ComponentsTable>{
        return componentsRepo.findAll()
    }

    /**
     * Find the entry of the DB by the ID
     * takes: Long
     * returns: ComponentsTable (entry)
     */
    fun getLineById(id: Long): ComponentsTable {
        if (this.componentsRepo.existsById(id)){
            return this.componentsRepo.findById(id).get()
        }else{
            return this.getDefault()
        }
    }

    /**
     * This function saves a new line in the sim_settings db
     */
    fun saveLine(s:ComponentsTable):ComponentsTable{
        var sim = ComponentsTable(s.readyToPlot, s.algorithm, s.sensor, s.insulinPump, s.virtualPatient, s.meals)
        return this.componentsRepo.save(s)
    }

    /**
     * This Function keeps the ID of the old item when updating
     */
    fun saveExistingLine(s:ComponentsTable):ComponentsTable{
        return this.componentsRepo.save(s)
    }

    /**
     * this function returns all stored entries from the components repository (DB)
     * as Iterable
     */
    fun getAll(): Iterable<ComponentsTable>{
    return this.componentsRepo.findAll()
    }

    fun deleteLineById(id: Long) {
        return this.componentsRepo.deleteById(id)

    }

    fun updateItem(item: ComponentsTable):ComponentsTable {
        this.deleteLineById(item.getId())
        return this.saveExistingLine(item)

    }


}