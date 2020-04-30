package com.silver.fox.stateslayout

enum class PageState constructor(internal var VALUE: String) {
    INITIALIZING("Initializing"),
    FAIL("Fail"),
    ERROR("Error"),
    NORMAL("Normal"),
    REFRESH("Refresh")
}