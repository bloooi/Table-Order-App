package com.eattalk.table.api.util

import com.eattalk.table.api.response.ProductEntry
import com.eattalk.table.api.response.StoreFullEntry

// 1) ProductEntry 에 시퀀스를 담을 래퍼 데이터 클래스
data class ProductWithSeq(
    val product: ProductEntry,
    val categoryId: String?,
    val sequenceInDisplay: Long
)
// 2) StoreEntry → List<ProductWithSeq> 변환 확장 함수
fun StoreFullEntry.toProductWithSeqList(): List<ProductWithSeq> {
    // productId → ProductEntry 빠른 조회를 위한 Map
    val productById = products.associateBy(ProductEntry::productId)

    return categories
        // 각 카테고리마다
        .flatMap { category ->
            // category.products: List<String>? 을 안전하게 순회
            category.products
                .orEmpty()
                // index: sequenceInDisplay, productId: 실제 ID
                .mapIndexed { index, prodId ->
                    val prod = productById[prodId]
                        ?: error("Unknown productId: $prodId")
                    ProductWithSeq(
                        product = prod,
                        categoryId = category.categoryId,
                        sequenceInDisplay = index.toLong()
                    )
                }
        }
}