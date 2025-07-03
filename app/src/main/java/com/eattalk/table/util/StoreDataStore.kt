package com.eattalk.table.util

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.eattalk.table.model.OperationState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class Store(
    val storeId: String,
    val name: String,
    val imageUrl: String? = null,
    val defaultLanguage: String,
    val currency: String,
    val supportLanguages: List<String>,
    val taxRate: String,
    val operation: OperationState,
    @Serializable(with = NullableLocalDateTimeIso8601Serializer::class)
    val openedAt: LocalDateTime?,
    @Serializable(with = NullableLocalDateTimeIso8601Serializer::class)
    val closedAt: LocalDateTime?,
    val stamps: List<Stamp>
){
    companion object {
        fun empty() = Store(
            storeId         = "",
            name            = "",
            imageUrl        = null,
            defaultLanguage = "",
            currency        = "",
            supportLanguages= emptyList(),
            taxRate         = "",
            operation = OperationState.CLOSED,
            openedAt        = null,
            closedAt        = null,
            stamps = emptyList()
        )
    }
}

@Serializable
data class Stamp(
    val stampId: String,
//    val type: DiscountUnit,
    val value: String,
    val quantity: Int,
)

object StoreSerializer : Serializer<Store> { // Store 객체가 없을 수도 있으므로 Store? 사용
    val json = Json {
        encodeDefaults = true // 기본값도 직렬화에 포함할지 여부
        isLenient = true // 느슨한 파싱 허용 (예: 따옴표 없는 키)
        prettyPrint = false // JSON 문자열을 예쁘게 출력할지 여부 (프로덕션에서는 false)
        ignoreUnknownKeys = true // 데이터 클래스에 없는 키는 무시
    }
    override val defaultValue: Store = Store.empty()
    override suspend fun readFrom(input: InputStream): Store {
        val bytes = input.readBytes()
        if (bytes.isEmpty()) return defaultValue

        return try {
            json.decodeFromString(Store.serializer(), bytes.decodeToString())
        } catch (e: SerializationException) {
            throw CorruptionException("Unable to deserialize Store", e)
        }
    }

    override suspend fun writeTo(t: Store, output: OutputStream) {
        output.write(json.encodeToString(Store.serializer(), t).encodeToByteArray())
    }
}
@Singleton
class StoreDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val Context.storeDataStore: DataStore<Store> by dataStore(
        fileName = Instant.DS_STORE_FILE_NAME,
        serializer = StoreSerializer
    )

    // 4) 읽기: Flow<Store> 로 구독
    val storeFlow: Flow<Store>
        get() = context.storeDataStore.data

    // 5) 쓰기: suspend 함수로 업데이트
    suspend fun saveStore(store: Store) =
        context.storeDataStore.updateData { store }

    suspend fun deleteStore() {
        context.storeDataStore.updateData { Store.empty() }
    }
}