package me.gistory.newbies_server_24.dto

import java.util.*

data class ApiInfoDto (
    val name: String,
    val version: String,
    val publishedAt: Date,
)