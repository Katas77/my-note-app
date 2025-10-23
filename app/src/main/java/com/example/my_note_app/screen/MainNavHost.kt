package com.example.my_note_app.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.my_note_app.viewmodel.NoteViewModel

@Composable
fun MainNavHost(viewModel: NoteViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { NoteScreen(viewModel,navController) }
        composable("search") { SearchScreen(viewModel,navController) }
    }
}