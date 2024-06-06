package com.example.APS_simulation_tool.repositories

import com.example.APS_simulation_tool.models.TodoItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoItemRepository: JpaRepository<TodoItem, Long>{
}