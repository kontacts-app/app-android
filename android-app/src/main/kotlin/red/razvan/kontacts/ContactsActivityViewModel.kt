package red.razvan.kontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import red.razvan.kontacts.repository.ContactId
import red.razvan.kontacts.repository.ContactsRepository
import red.razvan.kontacts.repository.NewContact

class ContactsActivityViewModel(
    private val repository: ContactsRepository,
) : ViewModel() {
    val sections =
        repository
            .observeSections()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val mutableLoading = MutableStateFlow(false)
    val loading = mutableLoading.asStateFlow()

    private val mutableNavigateToContactAction = MutableSharedFlow<ContactId>()
    val navigateToContactDetailAction = mutableNavigateToContactAction.asSharedFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.refreshContacts()
            }
        }
    }

    fun createNewContact(contactName: String) {
        viewModelScope.launch {
            mutableLoading.emit(true)
            val id =
                withContext(Dispatchers.IO) {
                    val newContact = NewContact(contactName)
                    repository.add(newContact)
                }
            mutableLoading.emit(false)
            mutableNavigateToContactAction.emit(id)
        }
    }
}
