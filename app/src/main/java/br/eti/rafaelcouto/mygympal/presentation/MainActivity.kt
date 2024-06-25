package br.eti.rafaelcouto.mygympal.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.eti.rafaelcouto.mygympal.R
import br.eti.rafaelcouto.mygympal.data.structure.CircularLinkedList
import br.eti.rafaelcouto.mygympal.domain.model.Exercicio
import br.eti.rafaelcouto.mygympal.domain.model.Grupo
import br.eti.rafaelcouto.mygympal.presentation.viewmodel.ExercicioViewModel
import br.eti.rafaelcouto.mygympal.presentation.viewmodel.ExerciciosViewModel
import br.eti.rafaelcouto.mygympal.presentation.viewmodel.GrupoViewModel
import br.eti.rafaelcouto.mygympal.presentation.viewmodel.GruposViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        object Rotas {
            const val LISTA_GRUPOS = "grupos"
            const val CAD_GRUPO = "grupo"
            const val EDIT_GRUPO = "grupo/{${Args.ID_GRUPO}}"

            const val LISTA_EXERCICIOS = "{${Args.ID_GRUPO}}/exercicios"
            const val CAD_EXERCICIO = "{${Args.ID_GRUPO}}/exercicio"
            const val EDIT_EXERCICIO = "{${Args.ID_GRUPO}}/exercicio/{${Args.ID_EXERCICIO}}"

            fun listaExercicios(idGrupo: Long) = "$idGrupo/exercicios"
            fun editGrupo(idGrupo: Long) = "grupo/$idGrupo"
            fun cadExercicio(idGrupo: Long) = "$idGrupo/exercicio"
            fun editExercicio(idGrupo: Long, idExercicio: Long) = "$idGrupo/exercicio/$idExercicio"
        }

        object Args {
            const val ID_GRUPO = "idGrupo"
            const val ID_EXERCICIO = "idExercicio"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController, startDestination = Rotas.LISTA_GRUPOS) {
                composable(route = Rotas.LISTA_GRUPOS) {
                    GruposScreen(navController = navController)
                }
                composable(route = Rotas.CAD_GRUPO) {
                    GrupoScreen(navController = navController)
                }
                composable(
                    route = Rotas.EDIT_GRUPO,
                    arguments = listOf(
                        navArgument(Args.ID_GRUPO) { type = NavType.LongType }
                    ), content = {
                        GrupoScreen(
                            navController = navController,
                            idGrupo = it.arguments?.getLong(Args.ID_GRUPO)
                        )
                    }
                )
                composable(
                    route = Rotas.LISTA_EXERCICIOS,
                    arguments = listOf(
                        navArgument(Args.ID_GRUPO) { type = NavType.LongType }
                    ), content = {
                        ExerciciosScreen(
                            idGrupo = it.arguments?.getLong(Args.ID_GRUPO) ?: 0L,
                            navController = navController
                        )
                    }
                )
                composable(
                    route = Rotas.CAD_EXERCICIO,
                    arguments = listOf(
                        navArgument(Args.ID_GRUPO) { type = NavType.LongType }
                    ), content = {
                        ExercicioScreen(
                            idGrupo = it.arguments?.getLong(Args.ID_GRUPO) ?: 0L,
                            navController = navController
                        )
                    }
                )
                composable(
                    route = Rotas.EDIT_EXERCICIO,
                    arguments = listOf(
                        navArgument(Args.ID_GRUPO) { type = NavType.LongType },
                        navArgument(Args.ID_EXERCICIO) { type = NavType.LongType }
                    ), content = {
                        ExercicioScreen(
                            idGrupo = it.arguments?.getLong(Args.ID_GRUPO) ?: 0L,
                            navController = navController,
                            idExercicio = it.arguments?.getLong(Args.ID_EXERCICIO)
                        )
                    }
                )
            }
        }
    }

    @Composable
    fun GruposScreen(navController: NavController) {
        val viewModel: GruposViewModel by viewModels()
        val owner = LocalLifecycleOwner.current
        val awareFlow = remember(viewModel.grupos, owner) {
            viewModel.grupos.flowWithLifecycle(owner.lifecycle, Lifecycle.State.STARTED)
        }

        val grupos: CircularLinkedList<Grupo> by awareFlow.collectAsState(initial = CircularLinkedList())

        MyGymPalScreen(
            navController = navController,
            title = stringResource(id = R.string.home_titulo),
            withBackButton = false,
            fab = {
                Fab(onClick = { navController.navigate(Rotas.CAD_GRUPO) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.cad_grupo_content_description)
                    )
                }
            }, content = { paddingValues ->
                EmptyMessage(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                    visible = grupos.isEmpty(),
                    text = stringResource(id = R.string.home_vazio_msg)
                )
                ListGrupos(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                    grupos = grupos,
                    navController = navController
                )
            }
        )
    }

    @Composable
    fun ListGrupos(modifier: Modifier, grupos: CircularLinkedList<Grupo>, navController: NavController) {
        if (grupos.isNotEmpty()) {
            LazyColumn(
                modifier = modifier,
                content = {
                    items(grupos.size) { index ->
                        val grupo = grupos[index].value
                        GrupoItem(
                            modifier = Modifier.clickable {
                                navController.navigate(route = Rotas.listaExercicios(idGrupo = grupo.id))
                            }, grupo = grupo
                        )
                        HorizontalDivider(color = colorResource(id = R.color.colorPrimaryAlpha))
                    }
                }
            )
        }
    }

    @Composable
    fun GrupoItem(modifier: Modifier, grupo: Grupo) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = if (grupo.ultimo) colorResource(id = R.color.mediumGrey) else Color.Transparent
                ), content = {
                    Text(
                        text = grupo.nome,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.padding_m),
                                vertical = dimensionResource(id = R.dimen.padding_p)
                            ), fontWeight = if (grupo.ultimo) FontWeight.Bold else FontWeight.Normal
                    )
            }
        )
    }

    @Composable
    fun GrupoScreen(navController: NavController, idGrupo: Long? = null) {
        val viewModel: GrupoViewModel by viewModels()
        viewModel.resetaDados()

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val msgSucesso = stringResource(
            id = if (idGrupo == null) R.string.cad_grupo_sucesso else R.string.edit_grupo_sucesso
        )

        idGrupo?.let { viewModel.carregaGrupo(it) }

        MyGymPalScreen(
            navController = navController,
            title = stringResource(id = if (idGrupo == null) R.string.cad_grupo_titulo else R.string.edit_grupo_titulo),
            withBackButton = true,
            content = { paddingValues ->
                BottomButtonColumn(
                    paddingValues = paddingValues,
                    content = {
                        TextField(
                            value = viewModel.nomeGrupo,
                            label = stringResource(id = R.string.cad_grupo_campo_nome),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            ), onValueChange = {
                                viewModel.nomeGrupo = it
                                viewModel.habilitaBotao = it.isNotBlank()
                            }
                        )
                    },
                    buttonTitle = stringResource(id = R.string.cad_grupo_salvar),
                    buttonEnabled = viewModel.habilitaBotao,
                    buttonClick = {
                        coroutineScope.launch {
                            viewModel.salvaGrupo()
                            Toast.makeText(context, msgSucesso, Toast.LENGTH_LONG).show()
                            navController.popBackStack()
                        }
                    }
                )
            }
        )
    }

    @Composable
    fun ExerciciosScreen(idGrupo: Long, navController: NavController) {
        val viewModel: ExerciciosViewModel by viewModels()
        viewModel.idGrupo = idGrupo
        viewModel.resetaSeries()

        val owner = LocalLifecycleOwner.current
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        val exercicios = viewModel.listaExercicios(idGrupo).collectAsStateWithLifecycle(
            owner = owner,
            initial = emptyList()
        )
        val grupo = viewModel.carregaGrupo().collectAsStateWithLifecycle(
            owner = owner,
            initial = Grupo(0, stringResource(id = R.string.lista_exercicios_titulo_padrao))
        )

        var podeConcluir by remember { mutableStateOf(true) }
        val msgSucessoConcluir = stringResource(id = R.string.concluir_grupo_sucesso)
        val msgSucessoExcluir = stringResource(id = R.string.grupo_delete_sucesso)

        MyGymPalScreen(
            navController = navController,
            title = grupo.nome,
            withBackButton = true,
            actions = {
                if (exercicios.isNotEmpty()) {
                    IconButton(
                        enabled = podeConcluir,
                        onClick = {
                            coroutineScope.launch {
                                viewModel.concluiGrupo(exercicios)
                                viewModel.concluiSeries(exercicios)
                                Toast.makeText(context, msgSucessoConcluir, Toast.LENGTH_LONG).show()
                                podeConcluir = false
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(id = R.string.concluir_grupo_content_description)
                        )
                    }
                }

                MyGymPalDropdownMenu(
                    items = {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.grupo_menu_edit)) },
                            onClick = { navController.navigate(route = Rotas.editGrupo(idGrupo)) }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.grupo_menu_delete)) },
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.excluiGrupo()
                                    navController.popBackStack(Rotas.LISTA_GRUPOS, false)
                                    Toast.makeText(context, msgSucessoExcluir, Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    }
                )
            },
            fab = {
                Fab(onClick = { navController.navigate(route = Rotas.cadExercicio(idGrupo)) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.cad_exercicio_content_description)
                    )
                }
            },
            content = { paddingValues ->
                EmptyMessage(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                    visible = exercicios.isEmpty(),
                    text = stringResource(id = R.string.exercicios_vazio_msg)
                )
                ListaExercicios(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                    navController = navController,
                    exercicios = exercicios,
                    viewModel = viewModel,
                    aoConcluir = { podeConcluir = false }
                )

            }
        )
    }

    @Composable
    fun ListaExercicios(modifier: Modifier, navController: NavController, exercicios: List<Exercicio.UI>, viewModel: ExerciciosViewModel, aoConcluir: () -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        if (exercicios.isNotEmpty()) {
            val mExercicios by remember { mutableStateOf(exercicios) }
            var concluidos by remember { mutableStateOf(mExercicios.map { false }) }
            val msgSucesso = stringResource(id = R.string.concluir_grupo_sucesso)

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                content = {
                    items(mExercicios.size) { index ->
                        val exercicio = mExercicios[index]
                        ExercicioItem(
                            exercicio = exercicio,
                            viewModel = viewModel,
                            aoConcluir = { id ->
                                val novosConcluidos = mExercicios.mapIndexed { mIndex, item ->
                                    item.original.id == id || concluidos[mIndex]
                                }

                                concluidos = novosConcluidos

                                if (concluidos.all { it }) coroutineScope.launch {
                                    viewModel.concluiGrupo(mExercicios)
                                    Toast.makeText(context, msgSucesso, Toast.LENGTH_LONG).show()
                                    aoConcluir()
                                }
                            }, navController = navController
                        )
                        HorizontalDivider(color = colorResource(id = R.color.colorPrimaryAlpha))
                    }
                }
            )
        }
    }

    @Composable
    fun ExercicioItem(exercicio: Exercicio.UI, viewModel: ExerciciosViewModel, aoConcluir: (Long) -> Unit, navController: NavController) {
        val context = LocalContext.current
        var carga by remember { mutableStateOf(exercicio.original.carga) }

        viewModel.iniciaSeriesConcluidas(exercicio = exercicio)

        val msgSucessoDec = stringResource(id = R.string.carga_reduzida_sucesso)
        val msgSucessoInc = stringResource(id = R.string.carga_aumentada_sucesso)
        val msgSucessoConcluir = stringResource(id = R.string.concluir_exercicio_sucesso)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_m),
                    vertical = dimensionResource(id = R.dimen.padding_p)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_pp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = exercicio.original.nome
                )
                RoundedButton(
                    onClick = {
                        navController.navigate(
                            route = Rotas.editExercicio(
                                idGrupo = exercicio.original.idGrupo,
                                idExercicio = exercicio.original.id
                            )
                        )
                    }, imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(id = R.string.edit_exercicio_content_description)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_p)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (exercicio.original.minRepeticoes == exercicio.original.maxRepeticoes) {
                    stringResource(
                        id = R.string.exercicio_reps,
                        exercicio.original.numSeries,
                        exercicio.original.minRepeticoes
                    )
                } else {
                    stringResource(
                        id = R.string.exercicio_reps_min_max,
                        exercicio.original.numSeries,
                        exercicio.original.minRepeticoes,
                        exercicio.original.maxRepeticoes
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = text
                )
                RoundedButton(
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_m)),
                    onClick = {
                        viewModel.reduzCarga(exercicio.original)
                        carga--
                        Toast.makeText(context, msgSucessoDec, Toast.LENGTH_SHORT).show()
                    }, imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.reduzir_carga_content_description)
                )
                Text(
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_m)),
                    text = stringResource(id = R.string.carga_kg, carga)
                )
                RoundedButton(
                    onClick = {
                        viewModel.aumentaCarga(exercicio.original)
                        carga++
                        Toast.makeText(context, msgSucessoInc, Toast.LENGTH_SHORT).show()
                    }, imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = stringResource(id = R.string.aumentar_carga_content_description)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_p)
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = stringResource(id = R.string.exercicio_series_concluidas)
                )
                viewModel.seriesConcluidas[exercicio.original.id]?.forEachIndexed { index, checked ->
                    MyGymPalCheckbox(
                        modifier = Modifier.padding(
                            start = if (index == 0) 0.dp else dimensionResource(id = R.dimen.padding_p)
                        ), checked = checked,
                        onCheckedChange = {
                            viewModel.concluiSerie(
                                idExercicio = exercicio.original.id,
                                index = index,
                                concluido = it
                            )

                            if (viewModel.seriesConcluidas[exercicio.original.id]?.all { finished -> finished } == true) {
                                viewModel.concluiExercicio(exercicio = exercicio)
                                aoConcluir(exercicio.original.id)
                                Toast.makeText(context, msgSucessoConcluir, Toast.LENGTH_LONG).show()
                            }
                        }, enabled = viewModel.podeConcluir[exercicio.original.id] ?: true
                    )
                }
            }
        }
    }

    @Composable
    fun ExercicioScreen(idGrupo: Long, navController: NavController, idExercicio: Long? = null) {
        val viewModel: ExercicioViewModel by viewModels()
        viewModel.resetaDados()

        val context = LocalContext.current
        var mIdExercicio by remember { mutableStateOf(idExercicio) }

        mIdExercicio?.let { viewModel.carregaExercicio(it) }

        val msgExcluiExercicio = stringResource(id = R.string.exclui_exercicio_sucesso)
        val msgCadExercicio = stringResource(
            id = if (idExercicio == null) R.string.cad_exercicio_sucesso else R.string.edit_exercicio_sucesso
        )

        MyGymPalScreen(
            navController = navController,
            title = stringResource(
                id = if (mIdExercicio == null) R.string.cad_exercicio_titulo else R.string.edit_exercicio_titulo
            ), withBackButton = true,
            actions = {
                mIdExercicio?.let {
                    IconButton(onClick = {
                        mIdExercicio = null
                        viewModel.excluiExercicio(it)
                        Toast.makeText(context, msgExcluiExercicio, Toast.LENGTH_LONG).show()
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.exclui_exercicio_content_description)
                        )
                    }
                }
            }
        ) { paddingValues ->
            BottomButtonColumn(
                paddingValues = paddingValues,
                content = {
                    TextField(
                        value = viewModel.nomeExercicio,
                        label = stringResource(id = R.string.cad_exercicio_campo_nome),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                        onValueChange = {
                            viewModel.nomeExercicio = it
                            viewModel.validaBotao()
                        }
                    )
                    TextField(
                        value = viewModel.numSeries,
                        label = stringResource(id = R.string.cad_exercicio_campo_series),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        onValueChange = {
                            viewModel.numSeries = it
                            viewModel.validaBotao()
                        }
                    )
                    TextField(
                        value = viewModel.minRepeticoes,
                        label = stringResource(id = R.string.cad_exercicio_campo_min_rep),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        onValueChange = {
                            viewModel.minRepeticoes = it
                            viewModel.validaBotao()
                        }
                    )
                    TextField(
                        value = viewModel.maxRepeticoes,
                        label = stringResource(id = R.string.cad_exercicio_campo_max_rep),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        onValueChange = {
                            viewModel.maxRepeticoes = it
                            viewModel.validaBotao()
                        }
                    )
                    TextField(
                        value = viewModel.carga,
                        label = stringResource(id = R.string.cad_exercicio_campo_carga),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        onValueChange = {
                            viewModel.carga = it
                            viewModel.validaBotao()
                        }
                    )
                }, buttonTitle = stringResource(id = R.string.cad_exercicio_salvar),
                buttonEnabled = viewModel.podeContinuar,
                buttonClick =  {
                    viewModel.salvaExercicio(idGrupo = idGrupo)
                    Toast.makeText(context, msgCadExercicio, Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            )
        }
    }

    // Components

    @Composable
    fun MyGymPalScreen(navController: NavController,
                       title: String,
                       withBackButton: Boolean,
                       actions: @Composable RowScope.() -> Unit = {},
                       fab: @Composable () -> Unit = {},
                       content: @Composable (PaddingValues) -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = title) },
                    navigationIcon = {
                        if (withBackButton)
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(id = R.string.voltar_content_description)
                                )
                            }
                    }, actions = actions,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.colorSecondary),
                        titleContentColor = colorResource(id = R.color.white),
                        navigationIconContentColor = colorResource(id = R.color.white),
                        actionIconContentColor = colorResource(id = R.color.white)
                    )
                )
            }, content = content,
            floatingActionButton = fab,
            floatingActionButtonPosition = FabPosition.End
        )
    }

    @Composable
    fun MyGymPalDropdownMenu(items: @Composable ColumnScope.() -> Unit) {
        var exibeMenu by remember { mutableStateOf(false) }

        IconButton(onClick = { exibeMenu = !exibeMenu }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(id = R.string.menu_opcoes)
            )
        }

        DropdownMenu(
            modifier = Modifier.background(color = colorResource(id = R.color.lightGrey)),
            expanded = exibeMenu,
            onDismissRequest = { exibeMenu = false },
            content = items
        )
    }

    @Composable
    fun BottomButtonColumn(paddingValues: PaddingValues, content: @Composable ColumnScope.() -> Unit, buttonTitle: String, buttonEnabled: Boolean, buttonClick: () -> Unit) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .weight(1f),
                content = content
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                BottomButton(
                    buttonTitle = buttonTitle,
                    buttonEnabled = buttonEnabled,
                    buttonClick = buttonClick
                )
            }
        }
    }

    @Composable
    fun TextField(value: String,
                  label: String,
                  keyboardOptions: KeyboardOptions,
                  onValueChange: (String) -> Unit) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_m),
                    vertical = dimensionResource(id = R.dimen.padding_p)
                ), label = { Text(text = label) },
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.colorSecondary),
                selectionColors = TextSelectionColors(
                    handleColor = colorResource(id = R.color.darkGrey),
                    backgroundColor = colorResource(id = R.color.mediumGrey)
                ),
                focusedTextColor = colorResource(id = R.color.black),
                unfocusedTextColor = colorResource(id = R.color.black),
                focusedLabelColor = colorResource(id = R.color.colorPrimary),
                unfocusedLabelColor = colorResource(id = R.color.black),
                focusedBorderColor = colorResource(id = R.color.colorPrimary),
                unfocusedBorderColor = colorResource(id = R.color.black)
            )
        )
    }

    @Composable
    fun EmptyMessage(modifier: Modifier, visible: Boolean, text: String) {
        if (visible) {
            Box(modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Text(text = text)
            }
        }
    }

    @Composable
    fun Fab(onClick: () -> Unit, content: @Composable () -> Unit) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = colorResource(id = R.color.colorPrimary),
            contentColor = colorResource(id = R.color.white),
            content = content
        )
    }

    @Composable
    fun BottomButton(buttonTitle: String, buttonEnabled: Boolean, buttonClick: () -> Unit) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_m)),
            onClick = buttonClick,
            enabled = buttonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.colorPrimary),
                contentColor = colorResource(id = R.color.white),
                disabledContainerColor = colorResource(id = R.color.colorPrimaryAlpha),
                disabledContentColor = colorResource(id = R.color.white)
            ), content = { Text(text = buttonTitle) }
        )
    }

    @Composable
    fun RoundedButton(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        imageVector: ImageVector,
        contentDescription: String
    ) {
        Button(
            modifier = modifier.size(size = dimensionResource(id = R.dimen.circle_button_size)),
            onClick = onClick,
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.colorPrimary),
                contentColor = colorResource(id = R.color.white),
                disabledContainerColor = colorResource(id = R.color.colorPrimaryAlpha),
                disabledContentColor = colorResource(id = R.color.white)
            ), content = {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription
                )
            }
        )
    }

    @Composable
    fun MyGymPalCheckbox(
        modifier: Modifier = Modifier,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        enabled: Boolean
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(
                modifier = modifier.padding(
                    vertical = dimensionResource(id = R.dimen.padding_p)
                ), checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.success),
                    uncheckedColor = colorResource(id = R.color.colorPrimary),
                    checkmarkColor = colorResource(id = R.color.white),
                    disabledCheckedColor = colorResource(id = R.color.successAlpha)
                )
            )
        }
    }
}
