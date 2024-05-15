package red.razvan.kontacts.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import red.razvan.kontacts.client.ContactsClient

class ContactsRepository(
    private val client: ContactsClient,
) {
    private val contacts: MutableStateFlow<Map<ContactId, Contact>> =
        MutableStateFlow(emptyMap())

    suspend fun refreshContacts() {
        val contacts =
            client
                .getAll()
                .map { it.toModel() }
        this.contacts.emit(contacts.associateBy { it.id })
    }

    suspend fun add(newContact: NewContact): ContactId {
        val created =
            client.addNewContact(newContact = newContact.toRemote())
                .toModel()
        contacts.update { value ->
            value + (created.id to Contact(id = created.id, name = created.name))
        }
        return created.id
    }

    suspend fun removeById(id: ContactId) {
        client.deleteById(id.toRemote())
        contacts.update { contacts ->
            contacts - id
        }
    }

    suspend fun update(contact: Contact) {
        val updated =
            client
                .updateById(id = contact.id.toRemote(), contact = RemoteNewContact(name = contact.name))
                .toModel()
        contacts.update { contacts ->
            contacts + (updated.id to updated)
        }
    }

    fun observeById(id: ContactId): Flow<Contact?> =
        contacts
            .map { contacts ->
                contacts[id]
            }

    fun observeSections(): Flow<List<ContactsSection>> =
        contacts
            .map { contacts ->
                contacts
                    .values
                    .asSequence()
                    .sortedBy(Contact::name)
                    .groupBy { contact ->
                        contact.name.first()
                    }
                    .map {
                        ContactsSection(initial = it.key, contacts = it.value)
                    }
                    .sortedBy(ContactsSection::initial)
            }
}
