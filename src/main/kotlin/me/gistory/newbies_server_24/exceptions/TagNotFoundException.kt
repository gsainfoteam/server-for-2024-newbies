package me.gistory.newbies_server_24.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND, reason = "Tag Not Found")
class TagNotFoundException : RuntimeException()