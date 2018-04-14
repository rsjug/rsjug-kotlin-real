package org.rsjug.todolist.controller.todoList

import org.jetbrains.exposed.sql.transactions.transaction
import org.rsjug.todolist.interceptor.database.ConnectToDatabase
import org.rsjug.todolist.model.todoList.TodoList
import org.rsjug.todolist.model.todoList.TodoListItem
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todoList")
class TodoListController {

    @GetMapping("")
    @ConnectToDatabase
    fun list() = transaction {
        TodoList.all().map { TodoListDTO(it) }
    }

    @GetMapping("/{id}")
    @ConnectToDatabase
    fun view(@PathVariable id: Long): TodoListWithItemDTO = transaction {
        val todoList = TodoList[id]
        TodoListWithItemDTO(todoList)
    }

    @PostMapping("")
    @ConnectToDatabase
    fun save(@RequestBody todoListDTO: TodoListDTO): TodoListDTO = transaction {
        val todoList = TodoList.new {
            description = todoListDTO.description
        }
        TodoListDTO(todoList)
    }

    @PostMapping("{id}/item")
    @ConnectToDatabase
    fun addItem(@PathVariable id: Long,
                @RequestBody todoListItemDTO: TodoListItemDTO): TodoListWithItemDTO = transaction {
        val todoListById = TodoList[id]
        TodoListItem.new {
            description = todoListItemDTO.description
            todoList = todoListById
        }
        TodoListWithItemDTO(todoListById)
    }

    @PutMapping("/{id}")
    @ConnectToDatabase
    fun update(@PathVariable id: Long,
               @RequestBody todoListDTO: TodoListDTO): TodoListDTO = transaction {
        val todoList = TodoList[id]
        todoList.description = todoListDTO.description
        TodoListDTO(todoList)
    }

    @DeleteMapping("/{id}")
    @ConnectToDatabase
    fun delete(@PathVariable id: Long): TodoListDTO = transaction {
        val todoList = TodoList[id]
        todoList.todoListItems.forEach { it.delete() }
        todoList.delete()
        TodoListDTO(todoList)
    }

    data class TodoListDTO(val id: Long,
                           val description: String) {
        constructor(todoList: TodoList) : this(todoList.id.value, todoList.description)
    }

    data class TodoListWithItemDTO(val id: Long,
                                   val description: String,
                                   val todoListItems: List<TodoListItemDTO>) {
        constructor(todoList: TodoList) : this(todoList.id.value, todoList.description,
                todoList.todoListItems.map { TodoListItemDTO(it) })
    }

    data class TodoListItemDTO(val id: Long, val description: String) {
        constructor(todoListItem: TodoListItem) : this(todoListItem.id.value,  todoListItem.description)
    }

}
