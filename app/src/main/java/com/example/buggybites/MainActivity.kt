package com.example.buggybites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BuggyBitesApp() }
    }
}

data class Order(val id: Int, val name: String, val isReady: Boolean = false)
enum class OrderFilter { ALL, READY, PREPARING }

class OrdersViewModel : ViewModel() {
    var orders by mutableStateOf(
        listOf(Order(1, "Veggie Burger"), Order(2, "Mango Smoothie", true), Order(3, "Paneer Wrap"))
    )
        private set
    var selectedFilter by mutableStateOf(OrderFilter.ALL)
        private set

    fun addOrder(name: String) {
        orders = orders + Order(id = (orders.maxOfOrNull { it.id } ?: 0) + 1, name = name)
    }

    fun toggleReady(id: Int) {
        orders = orders.map { if (it.id == id) it.copy(isReady = !it.isReady) else it }
    }

    fun deleteOrder(index: Int) {
        orders = orders.filterIndexed { currentIndex, _ -> currentIndex != index }
    }

    fun selectFilter(filter: OrderFilter) { selectedFilter = filter }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuggyBitesApp(viewModel: OrdersViewModel = viewModel()) {
    var showAddSheet by remember { mutableStateOf(false) }
    val visibleOrders = viewModel.orders // Filtering will be a future improvement.

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("BuggyBites") }) },
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddSheet = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add order")
                }
            }
        ) { padding ->
            Column(Modifier.padding(padding).fillMaxSize()) {
                Text(
                    text = "${viewModel.orders.size} orders today",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                FilterRow(viewModel.selectedFilter, viewModel::selectFilter)
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(visibleOrders) { order ->
                        OrderCard(
                            order = order,
                            onReadyChange = { viewModel.toggleReady(order.id) },
                            onDelete = { viewModel.deleteOrder(visibleOrders.indexOf(order)) }
                        )
                    }
                }
            }
        }
    }
    if (showAddSheet) {
        AddOrderDialog(onDismiss = { showAddSheet = false }, onAdd = {
            viewModel.addOrder(it)
            showAddSheet = false
        })
    }
}

@Composable
private fun FilterRow(selected: OrderFilter, onSelected: (OrderFilter) -> Unit) {
    Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OrderFilter.entries.forEach { filter ->
            FilterChip(selected = selected == filter, onClick = { onSelected(filter) }, label = { Text(filter.name.lowercase().replaceFirstChar { it.uppercase() }) })
        }
    }
}

@Composable
private fun OrderCard(order: Order, onReadyChange: () -> Unit, onDelete: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = order.isReady, onCheckedChange = { onReadyChange() })
            Column(Modifier.weight(1f)) {
                Text(order.name, fontWeight = FontWeight.SemiBold)
                Text(if (order.isReady) "Ready for pickup" else "Preparing", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete ${order.name}") }
        }
    }
}

@Composable
private fun AddOrderDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New order") },
        text = { OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Customer order") }, singleLine = true) },
        confirmButton = { TextButton(onClick = { onAdd(name) }) { Text("Add") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
