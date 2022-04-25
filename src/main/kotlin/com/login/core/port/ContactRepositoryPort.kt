package com.login.core.port

import com.login.core.model.Contact
import com.login.infra.entity.ContactEntity

interface ContactRepositoryPort {
    fun getAllContactsRepository(): List<Contact?>
    fun getOneContactRepository(id: String): Contact?
    fun postContactRepository(contactEntity: ContactEntity): Contact
    fun putContactRepository(contactEntity: ContactEntity): Contact
    fun delContactRepository(id: String): String
}