package com.eattalk.table.hilt

import android.content.Context
import androidx.room.Room
import com.eattalk.table.room.AppDatabase
import com.eattalk.table.room.dao.CategoryManagementDao
import com.eattalk.table.room.dao.DiscountDao
import com.eattalk.table.room.dao.OptionGroupDao
import com.eattalk.table.room.dao.OrderDao
import com.eattalk.table.room.dao.ProductDao
import com.eattalk.table.room.dao.TableDao
import com.eattalk.table.room.dao.ZoneDao
import com.eattalk.table.room.repository.CategoryManagementRepository
import com.eattalk.table.room.repository.DiscountRepository
import com.eattalk.table.room.repository.OptionGroupRepository
import com.eattalk.table.room.repository.OrderRepository
import com.eattalk.table.room.repository.ProductRepository
import com.eattalk.table.room.repository.StoreRepository
import com.eattalk.table.room.repository.TableRepository
import com.eattalk.table.room.repository.ZoneRepository
import com.eattalk.table.room.util.Trigger
import com.eattalk.table.util.Instant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun trigger(): Trigger = Trigger()

    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, Instant.DB_NAME)
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryManagementDao =
        db.categoryManagementDao()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao =
        db.productDao()

    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao =
        db.orderDao()

    @Provides
    fun provideTableDao(db: AppDatabase): TableDao =
        db.tableDao()

    @Provides
    fun provideZoneDao(db: AppDatabase): ZoneDao =
        db.zoneDao()

    @Provides
    fun provideOptionGroupDao(db: AppDatabase): OptionGroupDao =
        db.optionGroupDao()

    @Provides
    fun provideDiscountDao(db: AppDatabase): DiscountDao =
        db.discountDao()

    @Provides
    fun provideCategoryManagementRepository(
        categoryManagementDao: CategoryManagementDao,
        trigger: Trigger
    ): CategoryManagementRepository =
        CategoryManagementRepository(categoryManagementDao, trigger)

    @Provides
    fun provideDiscountRepository(
        discountDao: DiscountDao,
        trigger: Trigger
    ): DiscountRepository =
        DiscountRepository(discountDao, trigger)

    @Provides
    fun provideOptionGroupRepository(
        optionGroupDao: OptionGroupDao,
        trigger: Trigger
    ): OptionGroupRepository =
        OptionGroupRepository(optionGroupDao, trigger)

    @Provides
    fun provideProductRepository(
        productDao: ProductDao,
        trigger: Trigger
    ): ProductRepository =
        ProductRepository(productDao, trigger)

    @Provides
    fun provideOrderRepository(
        orderDao: OrderDao,
        trigger: Trigger
    ): OrderRepository =
        OrderRepository(orderDao, trigger)

    @Provides
    fun provideTableRepository(
        tableDao: TableDao,
        trigger: Trigger
    ): TableRepository =
        TableRepository(tableDao, trigger)

    @Provides
    fun provideZoneRepository(
        zoneDao: ZoneDao,
        tableDao: TableDao,
        trigger: Trigger
    ): ZoneRepository =
        ZoneRepository(zoneDao, tableDao, trigger)

    @Provides
    fun provideStoreRepository(
        appDatabase: AppDatabase,
        trigger: Trigger,
    ): StoreRepository = StoreRepository(
        appDatabase,
        trigger
    )
}
