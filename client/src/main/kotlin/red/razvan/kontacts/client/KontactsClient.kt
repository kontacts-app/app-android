package red.razvan.kontacts.client

import kotlinx.coroutines.delay

class KontactsClient {
    private val contacts =
        mockContacts
            .associateBy { it.id }
            .toMutableMap()

    suspend fun getAll(): List<Contact> =
        network {
            contacts
                .values
                .toList()
        }

    suspend fun getById(id: String): Contact = network { contacts[id] ?: throw ResourceNotFoundException(id = id) }

    suspend fun deleteById(id: String) {
        network {
            if (contacts.remove(id) == null) {
                throw ResourceNotFoundException(id = id)
            }
        }
    }

    suspend fun updateById(
        id: String,
        contact: NewContact,
    ): Contact =
        network {
            if (!contacts.containsKey(id)) {
                throw ResourceNotFoundException(id = id)
            }
            val created = Contact(id = id, name = contact.name)
            contacts[id] = created
            created
        }

    suspend fun addNewContact(newContact: NewContact): Contact =
        network {
            val created = Contact(name = newContact.name)
            contacts[created.id] = created
            created
        }

    private companion object {
        suspend fun <T> network(producer: () -> T): T {
            delay(timeMillis = (100L..1000L).random())
            return producer()
        }
    }
}
