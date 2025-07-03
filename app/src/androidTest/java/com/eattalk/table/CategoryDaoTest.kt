package com.eattalk.table

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eattalk.table.room.Category
import com.eattalk.table.room.dao.CategoryManagementDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CategoryDaoTest : RoomDaoTest() {

    private lateinit var dao: CategoryManagementDao

    @Before
    fun initDao() { dao = db.categoryManagementDao() }

    @Test
    fun insert_and_read_back_category_with_translations() = runTest {
        /* ---------- given ---------- */
        val cat = Category(
            categoryId = "cat_1",
            storeId    = "store_1",
            translations = mapOf("ko" to "커피", "en" to "Coffee"),
            sequenceInDisplay = 0
        )
        db.categoryManagementDao().insertCategory(cat)       // “Internal” 은 예: @Insert DAO

        /* ---------- when  ---------- */
        val result = dao.getCategoriesWithTranslations("store_1").first()

        /* ---------- then  ---------- */
        assertEquals(1, result.size)
        with(result.first()) {
            assertEquals("cat_1", categoryId)
            // ‘translations’ 는 Relation 으로 묶였다고 가정
            val langs = translations.map { it.key }
            assertTrue(langs.containsAll(listOf("ko","en")))
        }
    }
}