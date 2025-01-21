package kz.bloom.ui.subscription.choose_company.content

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.subscription.choose_company.component.ChooseCompanyComponent

@Composable
fun ChooseCompanyContent(modifier: Modifier = Modifier, component: ChooseCompanyComponent) {

    val model by component.model.subscribeAsState()

    Column(
        modifier = modifier
            .padding(horizontal = 21.dp)
            .padding(top = 42.dp)
    ) {
        Icon(
            modifier = Modifier.clickable { component.navigateBack() },
            painter = painterResource(id = R.drawable.ic_arrow_back_black),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(33.dp))
        Text(
            text = "ВЫБЕРИТЕ ФЛОРИСТА",
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(35.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            model.companies.forEach { company ->
                CompanyItem(
                    name = company.companyName,
                    price = company.price,
                    branchId = company.branchId,
                    onBranchPicked = { branchId, price -> component.branchPicked(branchId = branchId, price = price) }
                )
                HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
private fun CompanyItem(
    modifier: Modifier = Modifier,
    name: String,
    price: String,
    branchId: Long,
    onBranchPicked: (branchId: Long, price: String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier
                .background(color = Color.Black)
                .clickable { onBranchPicked(branchId, price) }
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 5.dp, horizontal = 8.dp),
                    text = "выбрать",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = "$price BLM",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal)
        )
    }
}