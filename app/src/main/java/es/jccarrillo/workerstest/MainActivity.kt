package es.jccarrillo.workerstest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import es.jccarrillo.workerstest.ui.theme.WorkersTestTheme
import es.jccarrillo.workerstest.workers.WorkerScheduler
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkersTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content()


                }
            }
        }
    }
}

@Composable
private fun Content(vm: MainVM = viewModel()) {
    val state by vm.state.collectAsState()

    Column {

        WorkersStatus(
            state = state,
            changeSingleWorkerStatus = vm::changeSingleWorkerStatus,
            changePeriodicWorkerStatus = vm::changePeriodicWorkerStatus,
            onRefresh = vm::onRefresh
        )
    }
}

@Composable
fun WorkersStatus(
    state: MainState,
    changeSingleWorkerStatus: (Boolean) -> Unit,
    changePeriodicWorkerStatus: (Boolean) -> Unit,
    onRefresh: () -> Unit
) {
    WorkerStatus(
        name = "Single worker",
        on = state.singleWorkerOn,
        onChangeStatus = changeSingleWorkerStatus
    )
    WorkerStatus(
        name = "Periodic worker",
        on = state.periodicWorkerOn,
        onChangeStatus = changePeriodicWorkerStatus
    )

    LogText(list = state.log, onRefresh = onRefresh)
}

@Composable
fun WorkerStatus(
    name: String,
    on: Boolean,
    onChangeStatus: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 15.dp,
                    vertical = 5.dp
                )
        ) {
            Text(text = name)
            Switch(checked = on, onCheckedChange = onChangeStatus)
        }
    }
}

@Composable
fun LogText(list: List<String>, onRefresh: () -> Unit) {
    IconButton(onClick = onRefresh) {
        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
    }
    SelectionContainer {

        LazyColumn {
            itemsIndexed(list) { index, item ->
                val color = if (index % 2 == 0) {
                    Color.Gray
                } else {
                    Color.LightGray
                }
                Text(text = item, modifier = Modifier.background(color))
            }
        }
    }
}