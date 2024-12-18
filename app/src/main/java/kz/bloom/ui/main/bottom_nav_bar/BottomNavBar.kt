package kz.bloom.ui.main.bottom_nav_bar

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kz.bloom.R

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    selectedTab: TabItem,
    onTabSelected: (TabItem) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(
                    bottom = 38.dp,
                    top = 14.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                icon = R.drawable.ic_home,
                isSelected = selectedTab == TabItem.HOME,
                onClick = { onTabSelected(TabItem.HOME) }
            )
            NavBarItem(
                icon = R.drawable.ic_search,
                isSelected = selectedTab == TabItem.SEARCH,
                onClick = { onTabSelected(TabItem.SEARCH) }
            )
            NavBarItem(
                icon = R.drawable.ic_menu,
                isSelected = selectedTab == TabItem.MENU,
                onClick = { onTabSelected(TabItem.MENU) },
                isMenuIcon = true
            )
            NavBarItem(
                modifier = Modifier.size(24.dp),
                icon = R.drawable.ic_bag,
                isSelected = selectedTab == TabItem.MARKET,
                onClick = { onTabSelected(TabItem.MARKET) }
            )
            NavBarItem(
                modifier = Modifier.size(24.dp),
                icon = R.drawable.ic_user,
                isSelected =  selectedTab == TabItem.PROFILE,
                onClick = { onTabSelected(TabItem.PROFILE)}
            )
        }
    }
}

@Composable
fun NavBarItem(
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isMenuIcon: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(height = if (isMenuIcon) 15.dp else 24.dp)
            .width(width = if (isMenuIcon) 50.dp else 24.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Black
        )
        AnimatedVisibility(
            visible = isSelected,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 1.dp)
                .height(1.dp)
                .width(6.dp)
                .background(Color.Black)
        ) {}
    }
}