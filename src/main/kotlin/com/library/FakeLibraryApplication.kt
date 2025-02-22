package com.library

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FakeLibraryApplication

fun main(args: Array<String>) {
    runApplication<FakeLibraryApplication>(*args)
}
