@file:Suppress("ktlint:standard:filename")

package red.razvan.kontacts.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
)
