package red.razvan.kontacts.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers

object KontactsDatabaseFactory {
    fun create(builder: RoomDatabase.Builder<KontactsDatabase>): KontactsDatabase =
        builder
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
}

fun getKontactsDatabaseBuilder(context: Context): RoomDatabase.Builder<KontactsDatabase> =
    Room
        .databaseBuilder(
            context = context.applicationContext,
            name = context.applicationContext.getDatabasePath("kontacts.db").absolutePath,
        )
