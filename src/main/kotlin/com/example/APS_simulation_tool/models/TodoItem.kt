package com.example.APS_simulation_tool.models

import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant


@Entity
@Table(name = "todo_items")
class TodoItem(
               private var description: String ="",
               private var isComplete: Boolean = false,
               private var createdAt: Instant = Instant.now(),
               private var updatedAt: Instant = Instant.now()
               ): Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id : Long = 0

    fun getId():Long{
        return this.id
    }

    override fun toString():String{
        return String.format("TodoItem{id=%d, description='%s', isComplete='%s', createdAt='%s', updatedAt='%s'}",
                id, description, isComplete, createdAt, updatedAt)
    }

    fun getDescription(): String {
        return this.description
    }

    fun setDescription(description: String) {
        this.description = description
    }
    fun setIsComplete(set: Boolean){
        this.isComplete = set
    }
    fun getIsComplete():Boolean{
        return this.isComplete
    }

    fun setCreatedAt(now: Instant) {
        this.createdAt = now
    }

    fun setUpdatedAt(now: Instant) {
        this.createdAt = now
    }
    fun getCreatedAt():String {
        return this.createdAt.toString()
    }

    fun getUpdatedAt():String {
        return this.createdAt.toString()
    }

}