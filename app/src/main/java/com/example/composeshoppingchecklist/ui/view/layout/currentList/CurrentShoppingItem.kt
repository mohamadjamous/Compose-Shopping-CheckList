package com.example.composeshoppingchecklist.ui.view.layout.currentList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeshoppingchecklist.R
import com.example.composeshoppingchecklist.ui.model.SharedViewModel
import com.example.composeshoppingchecklist.ui.model.model.ShoppingListUi
import com.example.composeshoppingchecklist.ui.model.state.ScreenState
import com.example.composeshoppingchecklist.util.getDateFormat

@Composable
fun ShoppingListCurrentItem(
    sharedViewModel: SharedViewModel,
    post: ShoppingListUi
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            modifier = Modifier.clickable(onClick = {
                sharedViewModel.pushBackStack(
                    ScreenState.CurrentProductList(
                        post
                    )
                )
            })
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            ) {
                Text(
                    text = post.shoppingListName,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                )
                Column(horizontalAlignment = Alignment.End) {
                    IconButton(onClick = { sharedViewModel.updateShoppingList(post, true) }) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_archive_black_24dp),
                            "",
                            Modifier.wrapContentSize()
                        )
                    }
                    Text(
                        text = post.shoppingListTimestamp.getDateFormat(),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(end = 8.dp),
                        style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic)
                    )
                }
            }
        }
    }
}
