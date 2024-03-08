package com.teamsparta.moamoa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
class MoamoaApplication

fun main(args: Array<String>) {
    runApplication<MoamoaApplication>(*args)
}
