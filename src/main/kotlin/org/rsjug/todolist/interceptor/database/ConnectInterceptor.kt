package org.rsjug.todolist.interceptor.database

import org.jetbrains.exposed.sql.Database
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.sql.DataSource

@Component
class ConnectInterceptor(private val dataSource: DataSource) :
        HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest,
                           response: HttpServletResponse,
                           handler: Any): Boolean {
        when (handler) {
            is HandlerMethod ->
                if (handler.hasMethodAnnotation(ConnectToDatabase::class.java)) {
                    Database.connect(dataSource)
                }
        }

        return true
    }

}