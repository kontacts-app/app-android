@file:Suppress("ktlint:standard:filename")

package red.razvan.kontacts.client

class ResourceNotFoundException(
    id: String,
) : Exception("Resource with id = $id not found")
