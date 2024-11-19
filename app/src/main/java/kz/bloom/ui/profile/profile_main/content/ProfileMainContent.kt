package kz.bloom.ui.profile.profile_main.content

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.profile.profile_main.component.ProfileCategoryType
import kz.bloom.ui.profile.profile_main.component.ProfileMainComponent

@Composable
fun ProfileMainContent(
    modifier: Modifier = Modifier,
    component: ProfileMainComponent
) {
    val model by component.model.subscribeAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .padding(horizontal = 21.dp)
            ) {
                Row {
                    Text(
                        text = model.name.uppercase(),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_editpen),
                        contentDescription = null
                    )
                }
                HorizontalDivider()
            }
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(horizontal = 21.dp)
                    .padding(top = 22.dp)
                    .padding(paddingValues)
            ) {
                CoinBalanceContent(coinBalance = model.coinBalance)
                LazyColumn(
                    modifier = Modifier.padding(top = 35.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(model.profileCategories) { profileCategory ->
                        ProfileCategory(
                            modifier = Modifier,
                            categoryName = profileCategory.type.categoryName,
                            categoryIcon = profileCategory.type.icon,
                            type = profileCategory.type,
                            onCategoryClicked = { type -> component.onCategoryClick(type) }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ProfileCategory(
    modifier: Modifier = Modifier,
    type: ProfileCategoryType,
    onCategoryClicked: (type: ProfileCategoryType) -> Unit,
    @StringRes
    categoryName: Int,
    @DrawableRes
    categoryIcon: Int
) {
    Column(
        modifier = modifier.clickable { onCategoryClicked(type) },
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = categoryIcon), contentDescription = null)
            Text(
                text = stringResource(id = categoryName),
                style = MaterialTheme.typography.labelSmall,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_expand_right_24),
                contentDescription = null
            )
        }
        HorizontalDivider()
    }
}

@Composable
fun CoinBalanceContent(modifier: Modifier = Modifier, coinBalance: Double) {
    Box(modifier = modifier.border(width = 1.dp, shape = RectangleShape, color = Color.Black)) {
        Column(modifier = Modifier.padding(vertical = 13.dp, horizontal = 20.dp)) {
            Row(
                modifier.padding(bottom = 20.dp)
            ) {
                Text(
                    text = "МОИ СЧЕТ",
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = "управлять",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Icon(
                        modifier = Modifier.padding(top = 2.dp),
                        painter = painterResource(id = R.drawable.ic_expand_right_12),
                        contentDescription = null
                    )
                }
            }
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "$coinBalance BLM",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 35.sp
                    )
                )
                Text(
                    text = "Кэшбек 3% с каждого заказа",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}