package com.example.demo.service

import com.example.demo.Repository.MessageRepository
import com.example.demo.data.Message
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MessageService(private val db : MessageRepository) {
    fun findMessages() : List<Message> = db.findAll().toList()

    fun findMessageById(id: String): Message? = db.findByIdOrNull(id)

    fun save(message:Message): Message = db.save(message)
}