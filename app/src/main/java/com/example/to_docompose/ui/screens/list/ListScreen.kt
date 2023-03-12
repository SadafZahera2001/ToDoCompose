package com.example.to_docompose.ui.screens.list


import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.to_docompose.R
import com.example.to_docompose.ui.theme.FabBackgroundColor
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.example.to_docompose.util.Action
import com.example.to_docompose.util.SearchAppBarState
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel
) {

    LaunchedEffect(key1 = action){
        sharedViewModel.handleDatabaseAction(action = action)
    }



    val allTask by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchTasks.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTask.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTask.collectAsState()

    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        onComplete = { sharedViewModel.action.value = it },
        onUndoClicked = {
            sharedViewModel.action.value = it
        },
        taskTitle = sharedViewModel.title.value,
        action = action
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState,
                sharedViewModel = sharedViewModel
            )
        },
        content = {
            ListContent(
                allTasks = allTask,
                searchedTask = searchedTasks,
                searchAppBarState = searchAppBarState,
                navigateToTaskScreen = navigateToTaskScreen,
                lowPriorityTask = lowPriorityTasks,
                highPriorityTask = highPriorityTasks,
                sortState = sortState,
                onSwipeToDelete = { action, task ->
                    sharedViewModel.action.value = action
                    sharedViewModel.updateTaskField(selectedTask = task)
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }
            )
        },
        floatingActionButton = {
            ListFAB(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFAB(
//    navigateToTaskScreen: (Int) -> Unit
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        backgroundColor = MaterialTheme.colors.FabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(
                id = R.string.add_button
            ),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {


    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action = action, taskTitle = taskTitle),
                    actionLabel = setActionLabel(action = action)
                )
                undoDeleteTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    }
}

private fun setMessage(action : Action,taskTitle: String):String {
    return when(action){
        Action.DELETE_ALL -> "All Tasks Removed."
        else -> "${action.name}: $taskTitle"
    }
}
private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeleteTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed
        && action == Action.DELETE
    ) {
        onUndoClicked(Action.UNDO)
    }
}