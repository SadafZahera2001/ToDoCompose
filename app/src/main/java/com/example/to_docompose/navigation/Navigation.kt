package com.example.to_docompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.to_docompose.navigation.destination.listComposable
import com.example.to_docompose.navigation.destination.splashComposable
import com.example.to_docompose.navigation.destination.taskComposable
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Constants.SPLASH_SCREEN
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navController){
        Screens(navController = navController)
    }
    AnimatedNavHost(navController = navController,
        startDestination = SPLASH_SCREEN
    ){
        splashComposable(
            navigateToListScreen = screen.splash
        )
        listComposable(
            navigateToTaskScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            navigateToListScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
    }
}