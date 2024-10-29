package com.example.composeshoppingchecklist.ui.model

import androidx.annotation.OpenForTesting
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.composeshoppingchecklist.data.database.model.Product
import com.example.composeshoppingchecklist.data.database.model.ShoppingList
import com.example.composeshoppingchecklist.data.repository.ShoppingListRepository
import com.example.composeshoppingchecklist.data.status.ResultStatus
import com.example.composeshoppingchecklist.ui.model.mapper.asDomainModel
import com.example.composeshoppingchecklist.ui.model.mapper.asUiModel
import com.example.composeshoppingchecklist.ui.model.model.MutableScreenUi
import com.example.composeshoppingchecklist.ui.model.model.ProductUi
import com.example.composeshoppingchecklist.ui.model.model.ScreenUi
import com.example.composeshoppingchecklist.ui.model.model.ShoppingListUi
import com.example.composeshoppingchecklist.ui.model.state.ScreenState
import com.example.composeshoppingchecklist.ui.model.state.ShoppingListState
import com.example.composeshoppingchecklist.util.ext.addSourceInvoke
import com.example.composeshoppingchecklist.util.ext.mapNonNull
import kotlinx.coroutines.launch

@OpenForTesting
class SharedViewModel constructor(
    private val backStack: ScreenBackStack,
    private val shoppingListRepository: ShoppingListRepository,
) : ViewModel(), ScreenBackStack by backStack {

    private val currentScreenState = backStack.getCurrentScreen()

    private val _updateShoppingListLoading = MutableLiveData<Boolean>()
    private val updateShoppingListLoading: LiveData<Boolean> = _updateShoppingListLoading

    private val _deleteProductLoading = MutableLiveData<Boolean>()
    private val deleteProductLoading: LiveData<Boolean> = _deleteProductLoading

    private val _createProductLoading = MutableLiveData<Boolean>()
    private val createProductLoading: LiveData<Boolean> = _createProductLoading

    private val _updateProductLoading = MutableLiveData<Boolean>()
    private val updateProductLoading: LiveData<Boolean> = _updateProductLoading

    private val _createShoppingListLoading = MutableLiveData<Boolean>()
    private val createShoppingListLoading: LiveData<Boolean> = _createShoppingListLoading

    private val shoppingListStatus = mapNonNull(currentScreenState) {
        when (it) {
            is ScreenState.CurrentShoppingList -> ShoppingListState.CURRENT
            is ScreenState.ArchivedShoppingList -> ShoppingListState.ARCHIVED
            else -> null
        }
    }

    private val shoppingLists = shoppingListStatus.switchMap {
        when (it) {
            ShoppingListState.CURRENT -> shoppingListRepository.getCurrentShoppingList()
            ShoppingListState.ARCHIVED -> shoppingListRepository.getArchivedShoppingList()
        }
    }

    private val shoppingListsUi = mapNonNull(shoppingLists) {
        when (it) {
            is ResultStatus.Loading -> ResultStatus.Loading
            is ResultStatus.Success -> ResultStatus.Success(it.data.asUiModel())
            is ResultStatus.Error -> it
        }
    }

    private val selectedShoppingList = mapNonNull(currentScreenState) {
        when (it) {
            is ScreenState.CurrentProductList -> it.shoppingList
            is ScreenState.ArchivedProductList -> it.shoppingList
            else -> null
        }
    }

    private val productList = selectedShoppingList.switchMap {
        shoppingListRepository.getProductList(it.id)
    }

    private val productListUi = mapNonNull(productList) {
        when (it) {
            is ResultStatus.Loading -> ResultStatus.Loading
            is ResultStatus.Success -> ResultStatus.Success(it.data.asUiModel())
            is ResultStatus.Error -> it
        }
    }

    final var productUi = mutableStateOf<ProductUi?>(null)
        private set

    fun createShoppingList(name: String) {
        _createShoppingListLoading.value = true
        val shoppingList = ShoppingList(shoppingListName = name)

        viewModelScope.launch {
            shoppingListRepository.insertShoppingList(shoppingList)
            _createShoppingListLoading.value = false
        }
    }

    fun updateShoppingList(shoppingList: ShoppingListUi, isArchived: Boolean) {
        _updateShoppingListLoading.value = true
        val shoppingListDomain = shoppingList.copy(isArchived = isArchived).asDomainModel()
        viewModelScope.launch {
            shoppingListRepository.updateShoppingList(shoppingListDomain)
            _updateShoppingListLoading.value = false
        }
    }

    fun createProduct(name: String, quantity: Long, shoppingListId: Long) {
        _createProductLoading.value = true

        val product = Product(
            productName = name,
            productQuantity = quantity,
            shoppingListId = shoppingListId,
        )
        viewModelScope.launch {
            shoppingListRepository.insertProduct(product)
            _createProductLoading.value = false
        }
    }

    fun deleteProduct(product: ProductUi) {
        _deleteProductLoading.value = true
        viewModelScope.launch {
            shoppingListRepository.deleteProduct(product.asDomainModel())
            _deleteProductLoading.value = false
        }
    }

    fun updateProduct(product: ProductUi) {
        _updateProductLoading.value = true
        viewModelScope.launch {
            shoppingListRepository.updateProduct(product.asDomainModel())
            _updateProductLoading.value = false
        }
    }

    fun updateProductUiInfo(product: ProductUi) {
        productUi.value = product
    }

    private val _screenState = MediatorLiveData<MutableScreenUi>().apply {
        value = MutableScreenUi(ScreenState.CurrentShoppingList)
        addSource(currentScreenState) { value?.currentScreenState = it ?: return@addSource }
        addSource(selectedShoppingList) { value?.selectedShoppingList = it }
        addSourceInvoke(createShoppingListLoading) { value?.createShoppingListLoading = it }
        addSourceInvoke(updateShoppingListLoading) { value?.updateShoppingListLoading = it }
        addSourceInvoke(createProductLoading) { value?.createProductLoading = it }
        addSourceInvoke(deleteProductLoading) { value?.deleteProductLoading = it }
        addSourceInvoke(shoppingListsUi) { value?.shoppingListsUi = it }
        addSourceInvoke(productListUi) { value?.productListUi = it }
    }
    val screenState: LiveData<ScreenUi> = _screenState.map { it.copy() }
}
