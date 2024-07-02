package br.eti.rafaelcouto.gymbro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.eti.rafaelcouto.gymbro.R
import br.eti.rafaelcouto.gymbro.navigation.GymBroNavHost
import br.eti.rafaelcouto.gymbro.presentation.uistate.MainActivityUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                GymBroApp(navController = navController)
            }
        }
    }
}

@Composable
fun GymBroApp(
    navController: NavHostController = rememberNavController()
) {
    var mainState by remember {
        mutableStateOf(MainActivityUiState(titleRes = R.string.app_name))
    }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    GymBroApp(
        topAppBarTitle = mainState.title ?: stringResource(id = mainState.titleRes),
        showsBackButton = mainState.showsBackButton,
        floatingActionButton = mainState.floatingActionButton,
        topAppBarActions = mainState.topAppBarActions,
        onBackButtonPressed = navController::popBackStack,
        snackbarHostState = snackBarHostState,
        content = {
            GymBroNavHost(
                setMainActivityState = { mainState = it },
                showMessage = { message ->
                    scope.launch {
                        snackBarHostState.showSnackbar(message = message)
                    }
                },
                navController = navController
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymBroApp(
    topAppBarTitle: String = stringResource(id = R.string.my_workouts),
    showsBackButton: Boolean = false,
    floatingActionButton: @Composable () -> Unit = {},
    topAppBarActions: @Composable RowScope.() -> Unit = {},
    onBackButtonPressed: () -> Unit = {},
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = topAppBarTitle) },
                navigationIcon = {
                    if (showsBackButton)
                        IconButton(onClick = onBackButtonPressed) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.go_back)
                            )
                        }
                },
                actions = topAppBarActions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.colorSecondary),
                    titleContentColor = colorResource(id = R.color.white),
                    navigationIconContentColor = colorResource(id = R.color.white),
                    actionIconContentColor = colorResource(id = R.color.white)
                )
            )
        },
        floatingActionButton = {
            floatingActionButton()
        },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {
            Box(modifier = Modifier.padding(it)) {
                content()
            }
        }
    )
}
