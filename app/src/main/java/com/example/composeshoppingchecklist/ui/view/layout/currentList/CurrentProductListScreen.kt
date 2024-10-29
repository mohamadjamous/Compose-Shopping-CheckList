package com.example.composeshoppingchecklist.ui.view.layout.currentList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.composeshoppingchecklist.R
import com.example.composeshoppingchecklist.data.status.ResultStatus
import com.example.composeshoppingchecklist.ui.model.AmbientScreenState
import com.example.composeshoppingchecklist.ui.model.AmbientSharedViewModel
import com.example.composeshoppingchecklist.ui.model.model.ProductUi
import com.example.composeshoppingchecklist.ui.model.model.compose.CurrentProductListModel
import com.example.composeshoppingchecklist.ui.view.layout.EmptyScreen

@Composable
fun ProductListCurrentScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val sharedViewModel = AmbientSharedViewModel.current
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                backgroundColor = MaterialTheme.colors.primary,
                navigationIcon = {
                    IconButton(onClick = { sharedViewModel.popBackStack() }) {
                        Icon(ImageVector.vectorResource(R.drawable.ic_baseline_arrow_back_24), "")
                    }
                }
            )
        },
        content = { padding ->
            ScreenBody(padding)
            CreateProductDialog(padding)
            UpdateProductDialog(padding, sharedViewModel.productUi.value)
        },
        floatingActionButton = { Fab() }
    )
}

@Composable
private fun ScreenBody(padding : PaddingValues) {
    when (val result = AmbientScreenState.current.productListUi) {
        is ResultStatus.Loading -> LoadingScreen()
        is ResultStatus.Success -> SuccessScreen(result.data)
        is ResultStatus.Error -> ErrorScreen()
        else -> {}
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun SuccessScreen(productList: List<ProductUi>) {
    val sharedViewModel = AmbientSharedViewModel.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp, 8.dp, 8.dp, 96.dp)
            .verticalScroll(rememberScrollState())
    ) {
        productList.forEach { post -> ProductCurrentItem(sharedViewModel, post) {
            sharedViewModel.updateProductUiInfo(it)
            CurrentProductListModel.showUpdateDialogState.value = true
        } }
    }
}

@Composable
private fun ErrorScreen() {
    EmptyScreen(
        R.string.empty_view_product_list_title,
        R.string.empty_view_product_list_subtitle_text
    )
}

@Composable
private fun Fab() {
    FloatingActionButton(
        onClick = { CurrentProductListModel.showDialogState.value = true },
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = contentColorFor(MaterialTheme.colors.onSecondary),
        content = { Icon(ImageVector.vectorResource(R.drawable.ic_add_black_24dp), "") }
    )
}