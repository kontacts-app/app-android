package red.razvan.kontacts

import android.app.Application
import androidx.room.withTransaction
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import red.razvan.kontacts.client.KontactsClient
import red.razvan.kontacts.db.DatabaseTransactionRunner
import red.razvan.kontacts.db.KontactsDatabase
import red.razvan.kontacts.db.KontactsDatabaseFactory
import red.razvan.kontacts.db.getKontactsDatabaseBuilder
import red.razvan.kontacts.repository.ContactId
import red.razvan.kontacts.repository.ContactsRepository

class KontactsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KontactsApplication)

            modules(
                module {
                    single {
                        KontactsDatabaseFactory
                            .create(builder = getKontactsDatabaseBuilder(androidContext()))
                    }
                    single {
                        get<KontactsDatabase>().contactsDao()
                    }
                    single {
                        DatabaseTransactionRunner { block ->
                            get<KontactsDatabase>().withTransaction(block)
                        }
                    }
                    single {
                        ContactsRepository(
                            client = KontactsClient(),
                            contactsDao = get(),
                            databaseTransactionRunner = get(),
                        )
                    }
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
