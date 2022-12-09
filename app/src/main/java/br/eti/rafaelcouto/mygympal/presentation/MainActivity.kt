package br.eti.rafaelcouto.mygympal.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
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
            val EDIT_GRUPO = "grupo/{${Args.ID_GRUPO}}"

            val LISTA_EXERCICIOS = "{${Args.ID_GRUPO}}/exercicios"
            val CAD_EXERCICIO = "{${Args.ID_GRUPO}}/exercicio"
            val EDIT_EXERCICIO = "{${Args.ID_GRUPO}}/exercicio/{${Args.ID_EXERCICIO}}"

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
                FloatingActionButton(onClick = { navController.navigate(Rotas.CAD_GRUPO) }) {
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
                    }
                }
            )
        }
    }

    @Composable
    fun GrupoItem(modifier: Modifier, grupo: Grupo) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_m),
                    vertical = dimensionResource(id = R.dimen.padding_p)
                )
                .background(color = if (grupo.ultimo) Color.LightGray else Color.Transparent),
            text = grupo.nome
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
            title = stringResource(id = R.string.lista_exercicios_titulo, grupo.nome),
            withBackButton = true,
            actions = {
                if (exercicios.isNotEmpty()) {
                    IconButton(
                        enabled = podeConcluir,
                        onClick = {
                            coroutineScope.launch {
                                viewModel.concluiGrupo(exercicios)
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
                FloatingActionButton(onClick = { navController.navigate(route = Rotas.cadExercicio(idGrupo)) }) {
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
                    }
                }
            )
        }
    }

    @Composable // TODO revisar
    fun ExercicioItem(exercicio: Exercicio.UI, viewModel: ExerciciosViewModel, aoConcluir: (Long) -> Unit, navController: NavController) {
        val context = LocalContext.current
        var carga by remember { mutableStateOf(exercicio.original.carga) }
        var podeConcluir by remember { mutableStateOf(true) }

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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = exercicio.original.nome
                )
                Button(onClick = {
                    navController.navigate(
                        route = Rotas.editExercicio(
                            idGrupo = exercicio.original.idGrupo,
                            idExercicio = exercicio.original.id
                        )
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.edit_exercicio_content_description)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    content = {
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

                        Text(modifier = Modifier.fillMaxWidth(), text = text)
                    }
                )
                Button(
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_m)),
                    onClick = {
                        viewModel.reduzCarga(exercicio.original)
                        carga--
                        Toast.makeText(context, msgSucessoDec, Toast.LENGTH_SHORT).show()
                    }
                ) { Text(text = stringResource(id = R.string.reduzir_carga)) }
                Text(
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_m)),
                    text = stringResource(id = R.string.carga_kg, carga)
                )
                Button(
                    onClick = {
                        viewModel.aumentaCarga(exercicio.original)
                        carga++
                        Toast.makeText(context, msgSucessoInc, Toast.LENGTH_SHORT).show()
                    }
                ) { Text(text = stringResource(id = R.string.aumentar_carga)) }
            }
            Row {
                val initial = mutableListOf<Boolean>()

                for (i in 0 until exercicio.original.numSeries) {
                    initial.add(false)
                }

                var concluido by remember { mutableStateOf(initial.toList()) }

                concluido.forEachIndexed { index, checked ->
                    Checkbox(
                        modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_p)),
                        checked = checked,
                        onCheckedChange = {
                            val list = mutableListOf<Boolean>()

                            concluido.forEachIndexed { innerIndex, _ ->
                                list.add(
                                    if (index == innerIndex) it else concluido[innerIndex]
                                )
                            }

                            concluido = list.toList()

                            if (concluido.all { it }) {
                                viewModel.concluiExercicio(exercicio = exercicio)
                                podeConcluir = false
                                aoConcluir(exercicio.original.id)
                                Toast.makeText(context, msgSucessoConcluir, Toast.LENGTH_LONG).show()
                            }
                        }, enabled = podeConcluir
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
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = stringResource(id = R.string.voltar_content_description)
                                )
                            }
                    }, actions = actions
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
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.padding_m)),
                    onClick = buttonClick,
                    enabled = buttonEnabled,
                    content = { Text(text = buttonTitle) }
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
            keyboardOptions = keyboardOptions
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
}
