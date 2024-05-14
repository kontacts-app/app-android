package red.razvan.contactsmultiplatform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import red.razvan.contactsmultiplatform.repository.ContactsRepository
import red.razvan.contactsmultiplatform.repository.NewContact

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
