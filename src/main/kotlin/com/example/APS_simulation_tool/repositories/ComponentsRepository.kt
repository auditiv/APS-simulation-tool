package com.example.APS_simulation_tool.repositories

import com.example.APS_simulation_tool.models.ComponentsTable

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ComponentsRepository:JpaRepository<ComponentsTable,Long> {

}