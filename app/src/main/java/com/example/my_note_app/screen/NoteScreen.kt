package com.example.my_note_app.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Star
import com.example.my_note_app.viewmodel.NoteViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.my_note_app.data.model.Note

@Composable
fun NoteScreen(viewModel: NoteViewModel = viewModel(),navController: NavController) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    val notes by viewModel.notes.collectAsState(initial = emptyList())

    // Основной контейнер с мягким фоном
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // светло-голубой
            .padding(20.dp)
            .padding(top = 24.dp) // добавляем сверху
    ) {
        // Заголовок раздела
        Text(
            "Добавить заметку",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Поле для заголовка
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле для текста
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Текст") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            maxLines = 4
        )

        // Переключатель "Избранное"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Избранное", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isFavorite,
                onCheckedChange = { isFavorite = it }
            )
        }

        // Кнопка сохранения
        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    viewModel.addNote(title, content, isFavorite)
                    title = ""
                    content = ""
                    isFavorite = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            enabled = title.isNotBlank() && content.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Сохранить", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("search") }
        ,    modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        )

        {
            Text("Перейти к поиску и редактированию")
        }


        // Раздел "Список заметок"
        Text(
            "Список заметок",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Список заметок
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(notes.size) { index ->
                val note = notes[index]
                NoteItem2(
                    note = note,
                    onDelete = { viewModel.deleteNote(note) }
                )
            }
        }
    }
}

@Composable
fun NoteItem2(
    note: Note,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Можно добавить детали */ }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Удалить",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onDelete() }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            if (note.isFavorite) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Избранное",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Избранное", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
@Composable
fun AppNavigation(viewModel: NoteViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { NoteScreen(viewModel,navController) }
        composable("search") { SearchScreen(viewModel,navController) }
    }
}