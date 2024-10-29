package com.example.composeshoppingchecklist.di

import android.content.Context
import com.example.composeshoppingchecklist.data.database.ShoppingListDatabase
import com.example.composeshoppingchecklist.data.database.ShoppingListDatabaseDao
import com.example.composeshoppingchecklist.data.database.ShoppingListSource
import com.example.composeshoppingchecklist.data.database.ShoppingListSourceImpl
import com.example.composeshoppingchecklist.data.repository.ShoppingListRepository
import com.example.composeshoppingchecklist.data.repository.ShoppingListRepositoryImpl
import com.example.composeshoppingchecklist.ui.model.ScreenBackStack
import com.example.composeshoppingchecklist.ui.model.ScreenBackStackImpl
import com.example.composeshoppingchecklist.ui.model.SharedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiDataModule = module {
    viewModel { SharedViewModel(get(), get()) }
    single { createBackStack() }
}

val dataModule = module {
    single { createShoppingListSource(get(), get()) }
    single { createRepository(get(), get()) }
    single { createDatabase(get()) }
    single { createIoDispatcher() }
}

fun createBackStack(): ScreenBackStack = ScreenBackStackImpl()

fun createIoDispatcher() = Dispatchers.Default

fun createDatabase(context: Context): ShoppingListDatabaseDao {
    return ShoppingListDatabase.getInstance(context).shoppingListDatabaseDao
}

fun createRepository(
    shoppingListDatabase: ShoppingListSource,
    ioDispatcher: CoroutineDispatcher
): ShoppingListRepository {
    return ShoppingListRepositoryImpl(shoppingListDatabase, ioDispatcher)
}

fun createShoppingListSource(
    shoppingListDatabase: ShoppingListDatabaseDao,
    ioDispatcher: CoroutineDispatcher
): ShoppingListSource {
    return ShoppingListSourceImpl(shoppingListDatabase, ioDispatcher)
}