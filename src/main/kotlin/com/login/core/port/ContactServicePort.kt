package com.login.core.port

import com.login.core.model.Contact
import com.login.entry.dto.ContactDto

interface ContactServicePort {
    fun getAllContacts(): List<ContactDto?>
    fun getOneContact(id: String): ContactDto
    fun postContact(contact: Contact): ContactDto
    fun putContact(contact: Contact): ContactDto
    fun delContact(id: String): String
}