package com.example.APS_simulation_tool.services

import com.example.APS_simulation_tool.models.TodoItem
import com.example.APS_simulation_tool.repositories.TodoItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Optional

@Service

class TodoItemService (@Autowired private var todoItemRepo: TodoItemRepository){
    fun getAll(): Iterable<TodoItem>{
        return this.todoItemRepo.findAll()
    }
    fun getById(id: Long):Optional<TodoItem>{
        return this.todoItemRepo.findById(id)
    }
    fun save(todoItem:TodoItem):TodoItem{
        todoItem.setCreatedAt(Instant.now())
        todoItem.setUpdatedAt(Instant.now())
        return this.todoItemRepo.save(todoItem)
    }
    fun update(todoItem:TodoItem):TodoItem{
        todoItem.setUpdatedAt(Instant.now())
        return this.todoItemRepo.save(todoItem)
    }
    fun delete(todoItem: TodoItem){
        this.todoItemRepo.delete(todoItem)
    }
    fun findById(id: Long): Optional<TodoItem> {
        return todoItemRepo.findById(id) ?: throw IllegalAccessError("TodoItem id: $id not found")
    }

    fun update(todoItem: TodoItem, s: String) {
       todoItemRepo.findById(todoItem.getId()).get()
               .setDescription(s)
        todoItemRepo.findById(todoItem.getId()).get()
               .setUpdatedAt(Instant.now())

    }
    fun buildToDoFromCSVArray(csvArr:List<Array<String>>):List<TodoItem>{
        val res = mutableListOf<TodoItem>()
        for ( line in csvArr.drop(1)){
            var todoItem = TodoItem(line[1],line[2].toBoolean(), createdAt = Instant.parse(line[3]), Instant.now())
            res.add(todoItem)
        }
        return res

    }
    fun createHashMap(keys: List<String>, numbers: List<Int>): MutableMap<String, Int>
    {
        val hMap: MutableMap<String, Int> = LinkedHashMap()

        if(keys.size == numbers.size) // create hashmap
        { var i: Int = 0
            for (item in keys){
                hMap[item] = numbers[i]
                i += 1
            }


        }
        return hMap
    }



}