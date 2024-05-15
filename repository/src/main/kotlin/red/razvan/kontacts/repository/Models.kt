package red.razvan.kontacts.repository

data class ContactId(
    val value: String,
)

data class Contact(
    val id: ContactId,
    val name: String,
)

data class ContactsSection(
    val initial: Char,
    val contacts: List<Contact>,
)

data class NewContact(val name: String)
