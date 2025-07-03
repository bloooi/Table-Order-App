package com.eattalk.table.room.repository

import com.eattalk.table.room.CategoryProduct
import com.eattalk.table.room.Product
import com.eattalk.table.room.ProductOptionGroup
import com.eattalk.table.room.ProductTag
import com.eattalk.table.room.dao.ProductDao
import com.eattalk.table.room.util.Trigger
import com.eattalk.table.room.util.mergeTrigger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ProductRepository(
    val productDao: ProductDao,
    val trigger: Trigger,
) {
    fun getProductFull(productId: String) =
        mergeTrigger(
            trigger.productTrigger,
            trigger.productOptionGroupTrigger,
            trigger.categoryTrigger,
            trigger.categoryProductTrigger
        ).flatMapLatest {
            productDao.getProductFull(productId)
        }

    fun getCategoriesWithProducts(storeId: String) =
        mergeTrigger(
            trigger.categoryTrigger,
            trigger.productTrigger,
            trigger.productOptionGroupTrigger,
            trigger.productTagTrigger,
            trigger.optionGroupTrigger,
            trigger.optionTrigger,
        ).flatMapLatest {
            productDao.getCategoriesWithProductFulls(storeId)
        }

    suspend fun upsertProduct(
        product: Product,
        sequenceInDisplay: Long,
        categoryId: String?,
        optionGroupIds: List<String>?,
        tags: List<String>?
    ) {
        productDao.upsertProduct(product)
        categoryId?.let {
            productDao.upsertCategoryProduct(
                CategoryProduct(
                    categoryId = categoryId,
                    productId = product.productId,
                    sequenceInDisplay = sequenceInDisplay,
                )
            )
        }
        optionGroupIds?.map { id ->
            productDao.upsertProductOptionGroup(
                ProductOptionGroup(
                    productId = product.productId,
                    optionGroupId = id,
                )
            )
        }

        tags?.map {
            productDao.upsertProductTag(
                ProductTag(
                    productId = product.productId,
                    tag = it,
                )
            )
        }
        trigger.updateProductTrigger()
        trigger.updateCategoryProductTrigger()
        trigger.updateProductOptionGroupTrigger()
        trigger.updateProductTagTrigger()

    }


    suspend fun deleteProduct(productId: String) {
        productDao.deleteProduct(productId)
        trigger.updateProductTrigger()

    }

    suspend fun deleteCategoryProduct(categoryId: String) {
        productDao.deleteCategoryProduct(categoryId)
        trigger.updateCategoryProductTrigger()
    }

    fun getCategoryProduct(productId: String) =
        productDao.getCategoryProduct(productId)

}