package red.razvan.contactsmultiplatform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import red.razvan.contactsmultiplatform.repository.ContactsRepository
import red.razvan.contactsmultiplatform.repository.ContactsSection
import red.razvan.contactsmultiplatform.repository.NewContact

class ContactsActivityViewModel(
    private val repository: ContactsRepository,
) : ViewModel() {

    private val mutableSections = MutableStateFlow(emptyList<ContactsSection>())
    val sections = mutableSections.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            mutableSections.emit(repository.getAll())
        }
    }

    fun createNewContact(contactName: String) {
        val newContact = NewContact(contactName)
        repository.add(newContact)
        refreshData()
    }
}