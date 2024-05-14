package red.razvan.kontacts.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class ContactsRepository {
    private val contacts: MutableStateFlow<Map<ContactId, Contact>> =
        MutableStateFlow(mockContacts.associateBy(Contact::id))

    fun add(newContact: NewContact) {
        val id = ContactId()
        contacts.update { value ->
            value + (id to Contact(id = id, name = newContact.name))
        }
    }

    fun removeById(id: ContactId) {
        contacts.update { contacts ->
            contacts - id
        }
    }

    fun update(contact: Contact) {
        contacts.update { contacts ->
            contacts + (contact.id to contact)
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
