package red.razvan.kontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import red.razvan.kontacts.repository.ContactId
import red.razvan.kontacts.repository.ContactsRepository

class ContactActivityViewModel(
    private val id: ContactId,
    private val repository: ContactsRepository,
) : ViewModel() {
    val contact =
        repository.observeById(id = id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val mutableLoading = MutableStateFlow(false)
    val loading = mutableLoading.asStateFlow()

    // The toolbar refresh is not working, so we restart the activity
    private val mutableNavigateToContactAction = MutableSharedFlow<ContactId>()
    val navigateToContactAction = mutableNavigateToContactAction.asSharedFlow()

    private val mutableNavigateUpAction = MutableSharedFlow<Unit>()
    val navigateUpAction = mutableNavigateUpAction.asSharedFlow()

    fun editContact(contactName: String) {
        viewModelScope.launch {
            val updated = contact.first()?.copy(name = contactName) ?: return@launch
            loading {
                withContext(Dispatchers.IO) {
                    repository.update(updated)
                }
                mutableNavigateToContactAction.emit(updated.id)
            }
        }
    }

    fun deleteContact() {
        viewModelScope.launch {
            loading {
                withContext(Dispatchers.IO) {
                    repository.removeById(id = id)
                }
            }
            mutableNavigateUpAction.emit(Unit)
        }
    }

    private suspend inline fun loading(block: () -> Unit) {
        mutableLoading.emit(true)
        block()
        mutableLoading.emit(false)
    }
}
