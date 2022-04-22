package com.component.kotlintest.app.demain.commands

interface Command<out T> {
    suspend fun execute(): T
}