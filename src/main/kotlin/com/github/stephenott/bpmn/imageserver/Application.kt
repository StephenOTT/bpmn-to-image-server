package com.github.stephenott.bpmn.imageserver

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.github.stephenott.bpmn.imageserver")
                .mainClass(Application.javaClass)
                .start()
    }
}