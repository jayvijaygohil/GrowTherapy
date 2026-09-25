# Retrofit and Ktor network alternatives

Use the existing client and converter conventions. These snippets illustrate the transport boundary, not a complete service integration. Do not install both alternatives just to follow this reference.

## Shared transport shape

Assume `GET /api/items` returns a complete catalog as a JSON array with required `id` and `title` string fields. The serialization annotation belongs in data.

```kotlin
import kotlinx.serialization.Serializable

@Serializable
internal data class ItemDto(val id: String, val title: String)
```

Use this DTO with the configured kotlinx.serialization converter/plugin. Adapt it to the real API schema; do not default missing required fields merely to make decoding succeed.

## Retrofit for Android/JVM

```kotlin
import retrofit2.http.GET

internal interface ItemApi {
    @GET("api/items")
    suspend fun fetchSnapshot(): List<ItemDto>
}
```

Construct the service once at the composition boundary from a Retrofit instance with a base URL ending in `/`, the project's OkHttp client, and a compatible JSON converter. Retrofit supports suspending functions without a separate coroutine call adapter.

For this body-returning signature, non-success HTTP responses throw `HttpException`. If a contract needs headers or status inspection, keep `Response<T>` handling inside data. Distinguish transport, HTTP, and decoding failures according to the documented API. Map only failures the repository contract intentionally handles; propagate cancellation.

Inject `ItemApi` directly into a repository when sufficient. For the companion repository example, implement `CatalogRemote` with a Retrofit adapter that translates the service's documented availability failures into `CatalogUnavailableException`. Do not classify all HTTP statuses or all exceptions as unavailability.

## Ktor for shared KMP or Android

```kotlin
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class KtorItemService(private val client: HttpClient) {
    suspend fun fetchSnapshot(): List<ItemDto> =
        client.get("api/items").body()
}

// Supply a compatible engine dependency for each target, or inject an engine.
internal fun createCatalogClient(): HttpClient = HttpClient {
    expectSuccess = true
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    defaultRequest {
        url("https://api.example.com/")
    }
}
```

Choose a compatible engine for every target and own/close the client at the appropriate application or component boundary. `expectSuccess` enables unsuccessful-status validation; map applicable Ktor failures inside data according to the same repository contract used for Retrofit. Handle any service-specific success-envelope semantics explicitly.

Avoid automatically wrapping non-blocking suspending requests in `Dispatchers.IO`. Follow `kotlin-coroutines-flows` for actual blocking or substantial CPU work.

Keep authentication, timeouts, retry policy, and logging in the configured infrastructure boundary. Do not log credentials or sensitive payloads, and do not add retries without considering idempotency and request budgets.

## Integration checks

Verify the actual service schema, URL resolution, JSON conversion, status handling, cancellation, and DI wiring with the project's existing tools. The repository example and these alternatives intentionally leave database implementation and service-specific exception translation to the project; do not present the snippets as a standalone compiled application.
