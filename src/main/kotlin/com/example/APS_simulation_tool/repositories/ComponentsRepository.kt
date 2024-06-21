package com.example.APS_simulation_tool.repositories

import com.example.APS_simulation_tool.models.ComponentsTable

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository intefaces are necessary in order to use the H2 database with hibernate.
 */
@Repository
interface ComponentsRepository : JpaRepository<ComponentsTable, Long> {

}