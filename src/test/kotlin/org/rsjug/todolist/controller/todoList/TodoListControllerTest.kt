package org.rsjug.todolist.controller.todoList

import io.kotlintest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.objectMockk
import io.mockk.use
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.rsjug.todolist.model.todoList.TodoList
import org.rsjug.todolist.model.todoList.TodoListItem
import org.rsjug.todolist.model.todoList.TodoListItems

object TodoListControllerTest: Spek({
    describe("TodoListController") {
        Database.connect("jdbc:h2:mem:rsjug-koltin-todo-list", "org.h2.Driver")
        val todoListController = TodoListController()

        context("list") {
            it("should list all todo lists") {
                val todoLists = objectMockk(TodoList).use {
                    val todoList01 = mockk<TodoList>()
                    every { todoList01.id.value } returns 1
                    every { todoList01.description } returns "description01"
                    val todoList02 = mockk<TodoList>()
                    every { todoList02.id.value } returns 2
                    every { todoList02.description } returns "description02"

                    every { TodoList.all() } returns SizedCollection(listOf(todoList01, todoList02))

                    todoListController.list()
                }

                todoLists shouldBe listOf(
                        TodoListController.TodoListDTO(1, "description01"),
                        TodoListController.TodoListDTO(2, "description02")
                )
            }
        }
        context("view") {
            it("should load a TodoList by Id") {
                val todoLists = objectMockk(TodoList).use {
                    val id = 1L

                    val todoListItem = mockk<TodoListItem>()
                    every { todoListItem.id.value } returns 1
                    every { todoListItem.description } returns "item01"

                    val todoList = mockk<TodoList>()
                    every { todoList.id.value } returns 1
                    every { todoList.description } returns "description01"
                    every { todoList.todoListItems } returns SizedCollection(listOf(todoListItem))

                    every { TodoList[id] } returns todoList

                    todoListController.view(1)
                }

                todoLists shouldBe TodoListController.
                        TodoListWithItemDTO(1, "description01",
                                listOf(TodoListController.TodoListItemDTO(1, "item01")))
            }
        }
    }
})