package com.example.composeshoppingchecklist.ui.model.mapper

import com.example.composeshoppingchecklist.data.database.model.ShoppingList
import com.example.composeshoppingchecklist.ui.model.model.ShoppingListUi

fun List<ShoppingList>.asUiModel(): List<ShoppingListUi> {
    return map { it.asUiModel() }
}

fun ShoppingList.asUiModel(): ShoppingListUi {
    return ShoppingListUi(
        id = id,
        shoppingListName = shoppingListName,
        shoppingListTimestamp = shoppingListTimestamp,
        isArchived = isArchived
    )
}

fun ShoppingListUi.asDomainModel(): ShoppingList {
    return ShoppingList(
        id = id,
        shoppingListName = shoppingListName,
        shoppingListTimestamp = shoppingListTimestamp,
        isArchived = isArchived
    )
}