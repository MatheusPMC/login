package com.login.core.service

import com.login.core.mapper.ContactConverter
import com.login.core.model.Contact
import com.login.core.port.ContactRepositoryPort
import com.login.core.port.ContactServicePort
import com.login.entry.dto.ContactDto
import io.micronaut.context.annotation.Prototype

@Prototype
class ContactService(
    private val contactRepositoryPort: ContactRepositoryPort
): ContactServicePort {
    override fun getAllContacts(): List<ContactDto?> {
        var repository = contactRepositoryPort.getAllContactsRepository()
        return ContactConverter.contactListToContactDtoList(repository)
    }

    override fun getOneContact(id: String): ContactDto {
        val repository = contactRepositoryPort.getOneContactRepository(id)
        return ContactConverter.contactToContactDto(repository!!)
    }

    override fun postContact(contact: Contact): ContactDto {
        var repository = contactRepositoryPort.postContactRepository(ContactConverter.postContactToContactEntity(contact))
        return ContactConverter.contactToContactDto(repository)
    }

    override fun putContact(contact: Contact): ContactDto {
        val repository = contactRepositoryPort.putContactRepository(ContactConverter.contactToContactEntity(contact))
        return ContactConverter.contactToContactDto(repository)
    }

    override fun delContact(id: String): String {
        return contactRepositoryPort.delContactRepository(id)
    }
}