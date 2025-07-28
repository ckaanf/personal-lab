package com.example.demo.Repository

import com.example.demo.data.Message
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<Message, String> {
}