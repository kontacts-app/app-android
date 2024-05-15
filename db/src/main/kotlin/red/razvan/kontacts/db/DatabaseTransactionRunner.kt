package red.razvan.kontacts.db

fun interface DatabaseTransactionRunner {
    suspend operator fun invoke(block: suspend () -> Unit)
}
