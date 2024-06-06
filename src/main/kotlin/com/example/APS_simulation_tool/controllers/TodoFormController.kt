package com.example.APS_simulation_tool.controllers

import com.example.APS_simulation_tool.models.TodoItem
import com.example.APS_simulation_tool.services.TodoItemService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import java.time.Instant


@Controller
class TodoFormController(
        @Autowired var todoItemService: TodoItemService
) {
    @GetMapping("/create-todo")
    fun showCreateForm(todoItem: TodoItem): String{
        return "new-todo-item"
    }
    @PostMapping("/new_todo")
    fun createTodoItem(@Valid todoItem: TodoItem, result: BindingResult, model: Model ): String{
        var todo : TodoItem = TodoItem(todoItem.getDescription(), false,
                Instant.now(), Instant.now())

        todoItemService.save(todo)
        return "redirect:/"
    }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long, model: Model): String{

        val todoItem:TodoItem = todoItemService.findById(id).get()

        todoItemService.delete(todoItem)
        return "redirect:/"
    }

    @GetMapping("/edit/{id}")
    fun showUpdateView(@PathVariable("id") id: Long, model: Model): String{

        val todoItem:TodoItem = todoItemService.findById(id).get()
        model.addAttribute("todo", todoItem)
        return "/edit-todo"
    }
    @PostMapping("/update-todo/{id}")
    fun edit(@PathVariable("id") id: Long,@Valid todoItem:TodoItem, result:BindingResult, model: Model): String {

        val item: TodoItem = todoItemService.findById(id).get()
        item.setDescription(todoItem.getDescription())
        item.setIsComplete(todoItem.getIsComplete())
        item.setUpdatedAt(Instant.now())

        todoItemService.update(item)
        return "redirect:/"
    }




}

