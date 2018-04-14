package org.rsjug.todolist.model.todoList

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable

class TodoListItem(id: EntityID<Long>) : LongEntity(id) {
    var description by TodoListItems.description
    var todoList by TodoList referencedOn TodoListItems.todoList

    companion object : LongEntityClass<TodoListItem>(TodoListItems)
}

object TodoListItems : LongIdTable("todo_list_item", "id") {
    val description = varchar("description", 100)
    val todoList = reference("id_todo_list", TodoLists)
}