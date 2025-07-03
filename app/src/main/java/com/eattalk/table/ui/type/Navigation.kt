package com.eattalk.table.ui.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eattalk.table.R

sealed class Navigation (val route: String, @DrawableRes val icon: Int, @StringRes val label: Int) {
    object Product: Navigation(route = "product", icon = R.drawable.product, label = R.string.navigation_product)
    object Tables: Navigation(route = "table", icon = R.drawable.grid, label = R.string.navigation_tables)
    object Orders: Navigation(route = "order", icon = R.drawable.order, label = R.string.navigation_orders)
    object Settings: Navigation(route = "setting", icon = R.drawable.setting, label = R.string.navigation_settings)
    object Manage: Navigation(route = "manage", icon = R.drawable.management, label = R.string.navigation_manage)

    companion object {
        val all = listOf(
            Product, Tables, Orders, Settings,
//            Manage
        )
    }
}