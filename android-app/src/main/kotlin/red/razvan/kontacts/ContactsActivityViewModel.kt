package red.razvan.kontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import red.razvan.kontacts.repository.ContactsRepository
import red.razvan.kontacts.repository.NewContact

class ContactsActivityViewModel(
    private val repository: ContactsRepository,
) : ViewModel() {
    val sections =
        repository
            .observeSections()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun createNewContact(contactName: String) {
        val newContact = NewContact(contactName)
        repository.add(newContact)
    }
}
