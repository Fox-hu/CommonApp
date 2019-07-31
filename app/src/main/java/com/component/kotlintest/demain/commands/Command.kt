package com.component.kotlintest.demain.commands

interface Command<out T> {
    suspend fun execute(): T
}