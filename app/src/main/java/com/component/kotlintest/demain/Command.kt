package com.component.kotlintest.demain

interface Command<out T> {
    suspend fun execute(): T
}