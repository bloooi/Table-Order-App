package com.eattalk.table.api.websocket

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.eattalk.table.api.repository.StoreApi

import com.eattalk.table.api.response.WebSocketResponse
import com.eattalk.table.api.util.apiCollect
import com.eattalk.table.room.repository.CategoryManagementRepository
import com.eattalk.table.room.repository.DiscountRepository
import com.eattalk.table.room.repository.OptionGroupRepository
import com.eattalk.table.room.repository.OrderRepository
import com.eattalk.table.room.repository.ProductRepository
import com.eattalk.table.room.repository.ZoneRepository
import com.eattalk.table.util.SettingsDataStore
import com.eattalk.table.util.Store
import com.eattalk.table.util.StoreDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WebSocketService : LifecycleService(), DefaultLifecycleObserver {
    @Inject
    lateinit var webSocketManager: WebSocketManager

    @Inject
    lateinit var storeFlow: StateFlow<Store>

    @Inject
    lateinit var storeDataStore: StoreDataStore

    @Inject
    lateinit var settingDataStore: SettingsDataStore

    @Inject
    lateinit var storeApi: StoreApi

    @Inject
    lateinit var categoryRepository: CategoryManagementRepository

    @Inject
    lateinit var discountRepository: DiscountRepository

    @Inject
    lateinit var optionGroupRepository: OptionGroupRepository

    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var orderRepository: OrderRepository

    @Inject
    lateinit var zoneRepository: ZoneRepository

    private var beforeStoreId = ""
    private var isAppInForeground = false // 앱 포그라운드 상태 추적용 플래그
    private fun triggerConnection() {
        lifecycleScope.launch {
            storeFlow.collectLatest { store ->
                if (store.storeId.isBlank()) {
                    Log.w("WSService", "Store ID가 비어있어 웹소켓 연결을 시작할 수 없습니다.")
                    webSocketManager.disconnect()
                    return@collectLatest
                }

                if (beforeStoreId == store.storeId) {
                    return@collectLatest
                }

                Log.d("WSService", "Store ID 변경됨: ${store.storeId}. 웹소켓 연결 시작/재시작.")
                beforeStoreId = store.storeId

                Log.d("WSService", "Store ID($beforeStoreId)로 웹소켓 연결 시작/재시작.")

                // 이전 연결이 있다면 종료
                webSocketManager.disconnect()

                webSocketManager.connect {
                    val deferred = CompletableDeferred<String?>()
                    storeApi.websocket(
                        beforeStoreId,
                        settingDataStore.websocketLastAtFlow.firstOrNull()
                    ).apiCollect(
                        success = { response -> deferred.complete(response.data.url) },
                        error = { error ->
                            Log.e("WSService", "URL fetch failed", error.exception)
                            deferred.complete(null)
                        }
                    )
                    deferred.await()
                }
            }
        }
    }

    override fun onCreate() {
        super<LifecycleService>.onCreate()
        startForeground(9998, createNotification())
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        triggerConnection()


        lifecycleScope.launch {
            webSocketManager.observeConnectionStatus().collect {
                if (it == ConnectionStatus.ERROR || it == ConnectionStatus.DISCONNECTED) {
                    beforeStoreId = ""
                }

                if (it == ConnectionStatus.ERROR && isAppInForeground) {
//                    openErrorScreen()
                }
            }
        }

        lifecycleScope.launch {
            webSocketManager.observeIncomingMessages().collect { response ->
                processEventResponse(response)
            }
        }
    }

    override fun onDestroy() {
        super<LifecycleService>.onDestroy()
        webSocketManager.disconnect()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
        isAppInForeground = true

        if (webSocketManager.observeConnectionStatus().value == ConnectionStatus.ERROR && isAppInForeground) {
//            openErrorScreen()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
        isAppInForeground = false
    }

    private suspend fun processEventResponse(response: WebSocketResponse) {
        settingDataStore.saveWebsocketLastAt(response.createdAt)
//        when (response) {
//            is CategoryCreated -> {
//                val entity = response.payload.category.toEntity(
//                    storeId = storeFlow.value.storeId,
//                    sequenceInDisplay = Clock.System.now().toEpochMilliseconds()
//                )
//                categoryRepository.addCategory(entity)
//            }
//
//            is CategoryDeleted -> {
//                productRepository.deleteCategoryProduct(response.payload.categoryId)
//                categoryRepository.deleteCategory(response.payload.categoryId)
//            }
//
//            is CategorySequenceUpdated -> {
//                val sequenceInDisplay =
//                    response.payload.sequence.mapIndexed { index, categoryId -> categoryId to index }
//                categoryRepository.updateCategorySequences(sequenceInDisplay)
//            }
//
//            is CategoryUpdated -> {
//                val sequenceInDisplay =
//                    response.payload.sequence.mapIndexed { index, categoryId -> categoryId to index }
//                val entity = response.payload.category.toEntity(
//                    storeId = storeFlow.value.storeId,
//                    sequenceInDisplay = sequenceInDisplay
//                        .find { it.first == response.payload.category.categoryId }
//                        ?.second?.toLong() ?: Clock.System.now().toEpochMilliseconds()
//                )
//
//                categoryRepository.updateCategory(entity)
//                categoryRepository.updateCategorySequences(sequenceInDisplay)
//            }
//
//            is DiscountCreated -> {
//                val entity = response.payload.discount.toEntity(
//                    storeId = storeFlow.value.storeId,
//                    sequenceInDisplay = Clock.System.now().toEpochMilliseconds()
//                )
//
//                discountRepository.upsertDiscounts(entity)
//
//            }
//
//            is DiscountDeleted -> {
//                discountRepository.deleteDiscount(response.payload.discountId)
//            }
//
//            is DiscountSequenceUpdated -> {
//                val sequences =
//                    response.payload.sequence.mapIndexed { index, sequence -> sequence to index }
//                discountRepository.updateSequences(sequences)
//            }
//
//            is DiscountUpdated -> {
//                val sequenceInDisplay =
//                    response.payload.sequence.mapIndexed { index, sequence -> sequence to index }
//
//                val entity = response.payload.discount.toEntity(
//                    storeId = storeFlow.value.storeId,
//                    sequenceInDisplay = sequenceInDisplay
//                        .find { it.first == response.payload.discount.discountId }
//                        ?.second?.toLong() ?: Clock.System.now().toEpochMilliseconds()
//                )
//
//                discountRepository.upsertDiscounts(entity)
//                discountRepository.updateSequences(sequenceInDisplay)
//            }
//
//            is OptionGroupCreated -> {
//                val entity =
//                    response.payload.optionGroup.toWithOptionsEntity(storeFlow.value.storeId)
//                optionGroupRepository.upsertOptionGroupWithOptions(entity)
//            }
//
//            is OptionGroupDeleted -> {
//                optionGroupRepository.deleteOptionGroupWithOptions(response.payload.optionGroupId)
//            }
//
//            is OptionGroupUpdated -> {
//                val entity =
//                    response.payload.optionGroup.toWithOptionsEntity(storeFlow.value.storeId)
//                optionGroupRepository.upsertOptionGroupWithOptions(entity)
//            }
//
//            is OrderCreated -> {
//                val orderEntity = response.payload.order.toEntities(
//                    storeId = storeFlow.value.storeId,
//                    orderSessionId = response.payload.orderSessionId,
//                )
//
//                orderRepository.upsertFullOrder(
//                    orderEntity.first,
//                    orderEntity.second,
//                    orderEntity.third
//                )
//            }
//
//            is OrderDeleted -> {
//                // Deprecated
//                orderRepository.updateOrderState(response.payload.orderId, OrderType.Canceled.key)
//            }
//
//            is OrderDetailDecremented -> {}
//
//            is OrderDetailDeleted -> {
//                orderRepository.deleteOrderDetailFromId(response.payload.orderDetailId)
//            }
//
//            is OrderSessionDiscount -> {}
//
//            is OrderDetailDiscount -> {}
//            is OrderSessionCompleted -> {
//                val sessionId = response.payload.orderSessionId
//                orderRepository.deleteSessionAndOrders(sessionId)
//            }
//
//            is OrderSessionCreated -> {
//                val entity = response.payload.orderSession.toEntity()
//                orderRepository.upsertOrderSession(entity)
//            }
//
//            is OrderSessionUpdated -> {
//                val entity = response.payload.orderSession.toEntity()
//                orderRepository.upsertOrderSession(entity)
//            }
//
//            is OrderSessionAllOrderDeleted -> {
//                // 삭제가 아니라 취소 처리
//                val id = response.payload.orderSessionId
//                orderRepository.cancelAllOrders(id)
//            }
//
//            is OrderUpdated -> {
//                orderRepository.updateOrderState(
//                    response.payload.order.orderId,
//                    response.payload.order.state
//                )
//            }
//
//            is ProductCreated -> {
//                val entity = response.payload.product.toEntity()
//                productRepository.upsertProduct(
//                    product = entity,
//                    sequenceInDisplay = Clock.System.now().toEpochMilliseconds(),
//                    categoryId = response.payload.product.categoryId,
//                    optionGroupIds = response.payload.product.optionGroups,
//                    tags = response.payload.product.tags
//                )
//            }
//
//            is ProductDeleted -> {
//                productRepository.deleteProduct(response.payload.productId)
//            }
//
//            is ProductUpdated -> {
//                val entity = response.payload.product.toEntity()
//                val categoryProduct =
//                    productRepository.getCategoryProduct(response.payload.product.productId)
//                productRepository.upsertProduct(
//                    product = entity,
//                    sequenceInDisplay = categoryProduct.firstOrNull()
//                        ?.firstOrNull()?.sequenceInDisplay
//                        ?: Clock.System.now().toEpochMilliseconds(),
//                    categoryId = response.payload.product.categoryId,
//                    optionGroupIds = response.payload.product.optionGroups,
//                    tags = response.payload.product.tags
//                )
//            }
//
//            is StoreUpdated -> {
//                val entity = response.payload.store
//                val store = Store(
//                    storeId = entity.storeId,
//                    name = entity.name,
//                    imageUrl = entity.imageUrl,
//                    defaultLanguage = entity.defaultLanguage,
//                    currency = entity.currency,
//                    supportLanguages = entity.supportLanguages,
//                    taxRate = entity.taxRate,
//                    operation = storeFlow.value.operation,
//                    openedAt = storeFlow.value.openedAt,
//                    closedAt = storeFlow.value.closedAt,
//                    stamps = storeFlow.value.stamps
//                )
//                storeDataStore.saveStore(store)
//            }
//
//            is StoreOperationUpdated -> {
//                storeDataStore.saveStore(
//                    storeFlow.value.copy(
//                        operation = response.payload.operation,
//                        openedAt = response.payload.openedAt,
//                        closedAt = response.payload.closedAt
//                    )
//                )
//            }
//
//            is ZoneUpdated -> {
//                val entities = response.payload.zones.mapIndexed { index, zoneEntry ->
//                    zoneEntry.toWithSeatsEntity(
//                        storeId = storeFlow.value.storeId,
//                        sequence = index.toLong()
//                    )
//
//                }
//
//                entities.forEach { entity ->
//                    zoneRepository.upsertZoneWithSeats(entity.zone, entity.seats)
//                }
//            }
//        }
    }

    private fun createNotification(): Notification {
        val channelId = "ws_service_channel"
        // 채널 생성 (API 26+)
        val channel = NotificationChannel(
            channelId,
            "WebSocket Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply { description = "Keep WebSocket alive" }
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            channel
        )
        // Notification 객체 반환
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Eat Talk POS")
            .setOngoing(true)
            .build()
    }
}