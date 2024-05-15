package red.razvan.kontacts.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import red.razvan.kontacts.client.KontactsClient
import red.razvan.kontacts.db.ContactsDao
import red.razvan.kontacts.db.DatabaseTransactionRunner

class ContactsRepository(
    private val client: KontactsClient,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val contactsDao: ContactsDao,
) {
    suspend fun refreshContacts() {
        val remoteContacts =
            client
                .getAll()
        databaseTransactionRunner {
            contactsDao.deleteAll()
            contactsDao.insert(remoteContacts.map { it.toLocal() })
        }
    }

    suspend fun add(newContact: NewContact): ContactId {
        val remote = client.addNewContact(newContact = newContact.toRemote())
        val local = remote.toLocal()
        contactsDao.insert(local)
        return ContactId(value = local.id)
    }

    suspend fun removeById(id: ContactId) {
        client.deleteById(id.toRemote())
        contactsDao.deleteById(id.toLocal())
    }

    suspend fun update(contact: Contact) {
        val remote =
            client
                .updateById(id = contact.id.toRemote(), contact = RemoteNewContact(name = contact.name))
        val local = remote.toLocal()
        contactsDao.update(local)
    }

    fun observeById(id: ContactId): Flow<Contact?> =
        contactsDao
            .observeById(id = id.toLocal())
            .map { it?.toModel() }

    fun observeSections(): Flow<List<ContactsSection>> =
        contactsDao
            .observeSortedByName()
            .map { contacts ->
                contacts
                    .map { it.toModel() }
                    .groupBy { contact ->
                        contact.name.first()
                    }
                    .map {
                        ContactsSection(initial = it.key, contacts = it.value)
                    }
                    .sortedBy(ContactsSection::initial)
            }
}
