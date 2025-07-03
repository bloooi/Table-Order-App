package com.eattalk.table

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.eattalk.table.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

/**
 * 모든 DAO 테스트가 공통으로 물려쓸 베이스 클래스.
 * - in-memory DB 로드/해제
 * - 기본 CoroutineDispatcher 설정
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class RoomDaoTest {

    protected lateinit var db: AppDatabase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()     // 테스트에서는 허용
            .build()
    }

    @After
    fun tearDown() {
        db.close()
        Dispatchers.resetMain()
    }
}