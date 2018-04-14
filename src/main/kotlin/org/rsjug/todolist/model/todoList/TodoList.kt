package org.rsjug.todolist.model.todoList

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable

class TodoList(id: EntityID<Long>) : LongEntity(id) {
    var description by TodoLists.description
    val todoListItems by
        TodoListItem referrersOn TodoListItems.todoList

    companion object : LongEntityClass<TodoList>(TodoLists)
}

object TodoLists : LongIdTable("todo_list", "id") {
    val description = varchar("description", 100)
}