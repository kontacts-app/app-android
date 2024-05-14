package red.razvan.kontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import red.razvan.kontacts.repository.ContactId
import red.razvan.kontacts.repository.ContactsRepository

class ContactActivityViewModel(
    private val id: ContactId,
    private val repository: ContactsRepository,
) : ViewModel() {
    val contact =
        repository.observeById(id = id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun editContact(contactName: String) {
        viewModelScope.launch {
            val contact = contact.first()?.copy(name = contactName) ?: return@launch
            repository.update(contact)
        }
    }

    fun deleteContact() {
        viewModelScope.launch {
            repository.removeById(id = id)
        }
    }
}
