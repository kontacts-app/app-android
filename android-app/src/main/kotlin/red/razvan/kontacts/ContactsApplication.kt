package red.razvan.kontacts

import android.app.Application
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import red.razvan.kontacts.repository.ContactId
import red.razvan.kontacts.repository.ContactsRepository

class ContactsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                module {
                    single { ContactsRepository() }
                    viewModel { ContactsActivityViewModel(repository = get()) }
                    viewModel { (id: ContactId) ->
                        ContactActivityViewModel(
                            id = id,
                            repository = get(),
                        )
                    }
                },
            )
        }
    }
}
