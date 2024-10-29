package com.example.composeshoppingchecklist.ui.view.layout.currentList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.composeshoppingchecklist.R
import com.example.composeshoppingchecklist.ui.model.SharedViewModel
import com.example.composeshoppingchecklist.ui.model.model.ProductUi

@Composable
fun ProductCurrentItem(
    sharedViewModel: SharedViewModel,
    product: ProductUi,
    onClick: (ProductUi) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            modifier = Modifier.clickable(onClick = {
                onClick(product)
            }),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
            ) {
                Text(
                    text = product.productName,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp),
                )
                Text(
                    text = product.productQuantity.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp),
                )
                Column(horizontalAlignment = Alignment.End) {
                    IconButton(onClick = { sharedViewModel.deleteProduct(product) }) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_delete_forever_black_24dp),
                            "",
                            Modifier.wrapContentSize(),
                        )
                    }
                }
            }
        }
    }
}
