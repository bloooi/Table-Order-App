package com.eattalk.table.util

import android.content.Context
import com.eattalk.table.R

class ResourceKt(context: Context) {
    val close = context.getString(R.string.close)
    val continueButton = context.getString(R.string.continue_button)
    val cancel = context.getString(R.string.cancel)
    val no = context.getString(R.string.no)
    val remove = context.getString(R.string.remove)
    val complete = context.getString(R.string.complete)
    val delete = context.getString(R.string.delete)

    val closeAppTitle = context.getString(R.string.error_screen_title)
    val closeAppBody = context.getString(R.string.error_screen_body)
    val closeAppConfirm = context.getString(R.string.error_close_confirm)

    // Error Texts
    val errorValidationTitle = context.getString(R.string.err_validation_title)
    val errorValidationBody = context.getString(R.string.err_validation_body)
    val errorDefaultTitle = context.getString(R.string.err_default_title)
    val errorDefaultBody = context.getString(R.string.err_default_body)

    val tagNew = context.getString(R.string.tag_new)
    val tagRecommend = context.getString(R.string.tag_recommend)

    val missingTranslationTitle = context.getString(R.string.dialog_missing_translation_title)
    val missingTranslationBody = context.getString(R.string.dialog_missing_translation_body)

    val cancelOrderTitle = context.getString(R.string.dialog_cancel_order_title)
    val cancelOrderBody = context.getString(R.string.dialog_cancel_order_body)

    val removeCategoryTitle = context.getString(R.string.dialog_remove_category_title)
    val removeCategoryBody = context.getString(R.string.dialog_remove_category_body)

    val removeCategoryWithProductsTitle = context.getString(R.string.dialog_remove_category_with_products_title)
    val removeCategoryWithProductsBody = context.getString(R.string.dialog_remove_category_with_products_body)

    val unavailablePaymentTitle = context.getString(R.string.dialog_unavailable_pay_title)
    val unavailablePaymentBody = context.getString(R.string.dialog_unavailable_pay_body)

    val paymentCompleteTitle = context.getString(R.string.dialog_payment_complete_title)
    val paymentCompleteBody = context.getString(R.string.dialog_payment_complete_body)

    val deleteAllOrdersTitle = context.getString(R.string.dialog_delete_all_orders_title)
    val deleteAllOrdersBody = context.getString(R.string.dialog_delete_all_orders_body)

    val required = context.getString(R.string.required)
    val optional = context.getString(R.string.optional)

    val customDiscountName = context.getString(R.string.discount)
    val totalDiscountName = context.getString(R.string.total_discount_tag_name)

}