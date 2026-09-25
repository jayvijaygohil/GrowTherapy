# Local source of truth with explicit refresh

Use this example when a complete remote catalog replaces a local catalog. Do not apply its replacement semantics to pagination, deltas, or a cache containing unsynchronized local edits.

## Domain contract

```kotlin
import kotlinx.coroutines.flow.Flow

data class Item(val id: String, val title: String)

sealed interface RefreshOutcome {
    data object Updated : RefreshOutcome
    data object Unavailable : RefreshOutcome
}

interface ItemRepository {
    /** Observe stored catalog values; storage failures terminate collection. */
    fun observeItems(): Flow<List<Item>>

    /**
     * Replace the catalog from a complete remote snapshot.
     * Unavailable leaves stored values unchanged; cancellation propagates.
     * Storage failures propagate; Updated means the local commit completed.
     */
    suspend fun refresh(): RefreshOutcome
}
```

Use the project's established outcome naming. This example deliberately models only remote unavailability as a recoverable outcome; define other expected failures explicitly when the product needs them.

## Data contracts and implementation

```kotlin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal data class ItemEntity(val id: String, val title: String)

internal class CatalogUnavailableException(cause: Throwable) : Exception(cause)

internal interface CatalogRemote {
    // Translate only documented availability failures into
    // CatalogUnavailableException. Preserve cancellation and other failures.
    suspend fun fetchSnapshot(): List<ItemDto>
}

internal interface CatalogStore {
    fun observeAll(): Flow<List<ItemEntity>>

    // Atomically replace this catalog, including deletion of absent rows.
    // Roll back on failure; never expose a partially replaced catalog.
    suspend fun replaceSnapshot(items: List<ItemEntity>)
}

internal class DefaultItemRepository(
    private val remote: CatalogRemote,
    private val store: CatalogStore,
) : ItemRepository {
    override fun observeItems(): Flow<List<Item>> =
        store.observeAll().map { entities ->
            entities.map { Item(id = it.id, title = it.title) }
        }

    override suspend fun refresh(): RefreshOutcome {
        val snapshot = try {
            remote.fetchSnapshot()
        } catch (unavailable: CatalogUnavailableException) {
            return RefreshOutcome.Unavailable
        }

        val entities = snapshot.map { dto ->
            ItemEntity(id = dto.id, title = dto.title)
        }
        store.replaceSnapshot(entities)
        return RefreshOutcome.Updated
    }
}
```

Implement `CatalogStore.replaceSnapshot` using the database's transaction API. A successful empty snapshot clears this catalog; failed fetching does not. Validate IDs and duplicates according to the actual service contract before committing. Preserve causes in the infrastructure failure and use the project's reporting boundary if diagnostics are needed.

This example assumes one refresh at a time, one catalog owner, and no independent local edits. If those assumptions do not hold, add a deliberate ordering/version/conflict policy. Do not assume a transaction orders overlapping network responses.

`ItemDto` is defined in the network reference. `CatalogRemote` represents full-snapshot semantics; an adapter around either networking alternative must apply the service's actual status/failure contract. No use case is added merely to forward `refresh()`.

Verify: successful replacement removes absent rows, successful empty snapshots clear the catalog, unavailability preserves cached data, failed writes roll back, and cancellation propagates.
