package kz.bloom.ui.ui_components.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kz.bloom.R

@Composable
fun Snackbar(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { snackbarData ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp)
                .padding(top = 12.dp)
                .background(Color.White)
                .border(1.dp, MaterialTheme.colorScheme.error, shape = RectangleShape)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_warning_mark),
                    contentDescription = "Error Icon",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = snackbarData.visuals.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}