package com.example.my_note_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.my_note_app.data.db.AppDatabase
import com.example.my_note_app.screen.NoteScreen
import com.example.my_note_app.ui.theme.MynoteappTheme
import com.example.my_note_app.viewmodel.NoteViewModel
import com.example.my_note_app.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getInstance(this)
        val noteDao = db.noteDao()
        enableEdgeToEdge()
        setContent {
            MynoteappTheme {
                val viewModel: NoteViewModel = viewModel(
                    factory = NoteViewModelFactory(noteDao)
                )
                NoteScreen(viewModel)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MynoteappTheme {

    }
}