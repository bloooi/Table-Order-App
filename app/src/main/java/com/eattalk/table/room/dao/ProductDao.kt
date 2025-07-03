package com.eattalk.table.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.eattalk.table.room.CategoryProduct
import com.eattalk.table.room.CategoryWithProductFulls
import com.eattalk.table.room.Product
import com.eattalk.table.room.ProductFull
import com.eattalk.table.room.ProductOptionGroup
import com.eattalk.table.room.ProductTag
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing product and its related option groups.
 */
@Dao
interface ProductDao {

    @Transaction
    @Query(
        """
    SELECT * 
      FROM product
     WHERE product_id = :productId
  """
    )
    fun getProductFull(productId: String): Flow<ProductFull>

    /**
     * storeId에 속한 모든 카테고리와,
     *   각 카테고리에 매핑된 모든 상품의 ProductFull(카테고리, 태그, 옵션그룹+옵션, 번역 포함)
     * 을 한 번의 @Transaction 호출로 실시간 스트림으로 가져옵니다.
     */

    @Transaction
    @Query(
        """
        SELECT * 
          FROM category
         WHERE store_id = :storeId
         ORDER BY sequence_in_display
        """
    )
    fun getCategoriesWithProductFulls(
        storeId: String
    ): Flow<List<CategoryWithProductFulls>>

    @Upsert
    suspend fun upsertProduct(product: Product)

    @Query("DELETE FROM product WHERE product_id = :productId")
    suspend fun deleteProduct(productId: String)

    @Upsert
    suspend fun upsertProductOptionGroup(productOptionGroup: ProductOptionGroup)

    @Delete
    suspend fun deleteProductOptionGroup(productOptionGroup: ProductOptionGroup)

    /**
     * Inserts a CategoryProduct. If a conflict on primary keys occurs, replace the existing row.
     */
    @Upsert
    suspend fun upsertCategoryProduct(categoryProduct: CategoryProduct)

    /**
     * Deletes a specific CategoryProduct entry.
     */
    @Query("DELETE FROM category_product WHERE category_id = :categoryId")
    suspend fun deleteCategoryProduct(categoryId: String)

    @Query("SELECT * FROM category_product WHERE product_id = :productId")
    fun getCategoryProduct(productId: String): Flow<List<CategoryProduct>>

    @Query(
        """UPDATE category_product 
            SET sequence_in_display = :sequence 
            WHERE category_id = :categoryId AND product_id = :productId"""
    )
    suspend fun updateSequence(
        categoryId: String,
        productId: String,
        sequence: Int
    )

    @Upsert
    suspend fun upsertProductTag(productTag: ProductTag)

    @Delete
    suspend fun deleteProductTag(productTag: ProductTag)

}