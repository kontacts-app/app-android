package red.razvan.kontacts.repository

internal typealias RemoteContact = red.razvan.kontacts.client.Contact
internal typealias RemoteNewContact = red.razvan.kontacts.client.NewContact
internal typealias LocalContact = red.razvan.kontacts.db.Contact

internal fun Contact.toRemote(): RemoteContact =
    RemoteContact(
        id = id.value,
        name = name,
    )

internal fun RemoteContact.toLocal(): LocalContact =
    LocalContact(
        id = id,
        name = name,
    )

internal fun NewContact.toRemote(): RemoteNewContact = RemoteNewContact(name = name)

internal fun RemoteContact.toModel(): Contact =
    Contact(
        id = ContactId(id),
        name = name,
    )

internal fun RemoteNewContact.toModel(): NewContact =
    NewContact(
        name = name,
    )

internal fun ContactId.toRemote(): String = value

internal fun ContactId.toLocal(): String = value

internal fun LocalContact.toModel(): Contact =
    Contact(
        id = ContactId(value = id),
        name = name,
    )
