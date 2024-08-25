package me.gistory.newbies_server_24.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Board not found")
class BoardNotFoundException : RuntimeException()