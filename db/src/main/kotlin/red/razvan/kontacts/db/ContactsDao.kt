package red.razvan.kontacts.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {
    @Query("DELETE FROM contacts")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(contact: Contact): Long

    @Insert
    suspend fun insert(contacts: List<Contact>): List<Long>

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(contact: Contact)

    @Query("SELECT * FROM contacts ORDER BY name")
    fun observeSortedByName(): Flow<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    fun observeById(id: String): Flow<Contact?>
}
