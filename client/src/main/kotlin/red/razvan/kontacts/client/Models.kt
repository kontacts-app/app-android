package red.razvan.kontacts.client

import java.util.UUID

data class NewContact(val name: String)

data class Contact(val id: String, val name: String)

internal fun Contact(name: String): Contact =
    Contact(
        id = UUID.randomUUID().toString(),
        name = name,
    )
