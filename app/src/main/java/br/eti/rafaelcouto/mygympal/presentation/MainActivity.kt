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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "grupos") {
                composable(route = "grupos") { GruposScreen(navController = navController) }
                composable(route = "grupo") { GrupoScreen(navController = navController) }
                composable(
                    route = "grupo/{idGrupo}", arguments = listOf(navArgument("idGrupo") { type = NavType.LongType })
                ) { GrupoScreen(navController = navController, idGrupo = it.arguments?.getLong("idGrupo")) }
                composable(
                    route = "{idGrupo}/exercicios",
                    arguments = listOf(navArgument("idGrupo") { type = NavType.LongType })
                ) {
                    ExerciciosScreen(idGrupo = it.arguments?.getLong("idGrupo") ?: 0L, navController = navController)
                }
                composable(
                    route = "{idGrupo}/exercicio",
                    arguments = listOf(navArgument("idGrupo") { type = NavType.LongType })
                ) {
                    ExercicioScreen(idGrupo = it.arguments?.getLong("idGrupo") ?: 0L, navController = navController)
                }
                composable(
                    route = "{idGrupo}/exercicio/{idExercicio}",
                    arguments = listOf(
                        navArgument("idGrupo") { type = NavType.LongType },
                        navArgument("idExercicio") { type = NavType.LongType }
                    )
                ) {
                    ExercicioScreen(
                        idGrupo = it.arguments?.getLong("idGrupo") ?: 0L,
                        navController = navController,
                        idExercicio = it.arguments?.getLong("idExercicio")
                    )
                }
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

        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "Meus treinos") },)
            }, content = { paddingValues ->
                EmptyGrupos(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                    visible = grupos.isEmpty()
                )
                ListGrupos(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                    grupos = grupos,
                    navController = navController
                )
            }, floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("grupo") }) {
                    Icon(Icons.Filled.Add, "Cadastrar grupo")
                }
            }, floatingActionButtonPosition = FabPosition.End
        )
    }

    @Composable
    fun EmptyGrupos(modifier: Modifier, visible: Boolean) {
        if (visible) {
            Box(modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Text(text = "Não há grupos cadastrados")
            }
        }
    }

    @Composable
    fun ListGrupos(modifier: Modifier, grupos: CircularLinkedList<Grupo>, navController: NavController) {
        if (grupos.isNotEmpty()) {
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(grupos.size) { index ->
                    val grupo = grupos[index].value
                    GrupoItem(
                        modifier = Modifier.clickable {
                            navController.navigate("${grupo.id}/exercicios")
                        }, grupo = grupo
                    )
                }
            }
        }
    }

    @Composable
    fun GrupoItem(modifier: Modifier, grupo: Grupo) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
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

        idGrupo?.let { viewModel.carregaGrupo(it) }

        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = if (idGrupo == null) "Criar grupo" else "Editar grupo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        }, content = { paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                        FormItem(
                            value = viewModel.nomeGrupo,
                            label = "Nome do grupo",
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            ), onValueChange = {
                                viewModel.nomeGrupo = it
                                viewModel.habilitaBotao = it.isNotBlank()
                            }
                        )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClick = {
                            coroutineScope.launch {
                                viewModel.salvaGrupo()
                                Toast.makeText(
                                    context,
                                    if (idGrupo == null) "Grupo criado!" else "Grupo atualizado!",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.popBackStack()
                            }
                        }, enabled = viewModel.habilitaBotao) {
                        Text(text = "Salvar grupo")
                    }
                }
            }
        })
    }

    @Composable
    fun ExerciciosScreen(idGrupo: Long, navController: NavController) {
        val viewModel: ExerciciosViewModel by viewModels()
        viewModel.idGrupo = idGrupo

        val owner = LocalLifecycleOwner.current

        val flow = viewModel.listaExercicios(idGrupo)
        val awareFlow = remember(flow, owner) {
            flow.flowWithLifecycle(owner.lifecycle, Lifecycle.State.STARTED)
        }

        val grupoFlow = viewModel.carregaGrupo()
        val grupoAwareFlow = remember(grupoFlow, owner) {
            grupoFlow.flowWithLifecycle(owner.lifecycle, Lifecycle.State.STARTED)
        }

        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val exercicios: List<Exercicio.UI> by awareFlow.collectAsState(initial = emptyList())
        val grupo: Grupo by grupoAwareFlow.collectAsState(initial = Grupo(0, "Exercícios"))
        var podeConcluir by remember { mutableStateOf(true) }
        var exibeMenu by remember { mutableStateOf(false) }

        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = "Exercícios - ${grupo.nome}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Voltar")
                    }
                }, actions = {
                    if (exercicios.isNotEmpty()) {
                        IconButton(
                            enabled = podeConcluir,
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.concluiGrupo(exercicios)
                                    Toast.makeText(context, "Grupo concluído!", Toast.LENGTH_LONG).show()
                                    podeConcluir = false
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Done, "Concluir grupo")
                        }
                    }

                    IconButton(onClick = { exibeMenu = !exibeMenu }) {
                        Icon(Icons.Filled.MoreVert, "Opções")
                    }

                    DropdownMenu(
                        expanded = exibeMenu,
                        onDismissRequest = { exibeMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Editar") },
                            onClick = { navController.navigate("grupo/$idGrupo") }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Excluir") },
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.excluiGrupo()
                                    navController.popBackStack("grupos", false)
                                    Toast.makeText(context, "Grupo excluído!", Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    }
                }
            )
        }, content = { paddingValues ->
            EmptyExercicios(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                visible = exercicios.isEmpty()
            )
            ListaExercicios(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                navController = navController,
                exercicios = exercicios,
                viewModel = viewModel,
                aoConcluir = { podeConcluir = false }
            )
        }, floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("$idGrupo/exercicio") }) {
                Icon(Icons.Filled.Add, "Cadastrar exercício")
            }
        }, floatingActionButtonPosition = FabPosition.End)
    }

    @Composable
    fun EmptyExercicios(modifier: Modifier, visible: Boolean) {
        if (visible) {
            Box(modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Text(text = "Não há exercícios cadastrados")
            }
        }
    }

    @Composable
    fun ListaExercicios(modifier: Modifier, navController: NavController, exercicios: List<Exercicio.UI>, viewModel: ExerciciosViewModel, aoConcluir: () -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        if (exercicios.isNotEmpty()) {
            val mExercicios by remember { mutableStateOf(exercicios) }
            var concluidos by remember { mutableStateOf(mExercicios.map { false }) }

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                content = {
                    items(mExercicios.size) { index ->
                        ExercicioItem(
                            exercicio = mExercicios[index],
                            viewModel = viewModel,
                            aoConcluir = { id ->
                                val novosConcluidos = mExercicios.mapIndexed { mIndex, item ->
                                    item.original.id == id || concluidos[mIndex]
                                }

                                concluidos = novosConcluidos

                                if (concluidos.all { it })
                                    coroutineScope.launch {
                                        viewModel.concluiGrupo(mExercicios)
                                        Toast.makeText(context, "Grupo concluído!", Toast.LENGTH_LONG).show()
                                        aoConcluir()
                                    }
                            }, navController = navController
                        )
                    }
                }
            )
        }
    }

    @Composable
    fun ExercicioItem(exercicio: Exercicio.UI, viewModel: ExerciciosViewModel, aoConcluir: (Long) -> Unit, navController: NavController) {
        val context = LocalContext.current
        var carga by remember { mutableStateOf(exercicio.original.carga) }
        var podeConcluir by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
                Button(onClick = { navController.navigate("${exercicio.original.idGrupo}/exercicio/${exercicio.original.id}") }) {
                    Icon(Icons.Filled.Edit, "Editar exercício")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    val text = if (exercicio.original.minRepeticoes == exercicio.original.maxRepeticoes) {
                        "${exercicio.original.numSeries} x ${exercicio.original.minRepeticoes}"
                    } else {
                        "${exercicio.original.numSeries} x ${exercicio.original.minRepeticoes} - ${exercicio.original.maxRepeticoes}"
                    }

                    Text(modifier = Modifier.fillMaxWidth(), text = text)
                }
                Button(
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = {
                        viewModel.reduzCarga(exercicio.original)
                        carga--
                        Toast.makeText(context, "Carga reduzida", Toast.LENGTH_SHORT).show()
                    }
                ) { Text(text = "-") }
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = "${carga}kg"
                )
                Button(
                    onClick = {
                        viewModel.aumentaCarga(exercicio.original)
                        carga++
                        Toast.makeText(context, "Carga aumentada", Toast.LENGTH_SHORT).show()
                    }
                ) { Text(text = "+") }
            }
            Row {
                val initial = mutableListOf<Boolean>()

                for (i in 0 until exercicio.original.numSeries) {
                    initial.add(false)
                }

                var concluido by remember { mutableStateOf(initial.toList()) }

                concluido.forEachIndexed { index, checked ->
                    Checkbox(
                        modifier = Modifier.padding(end = 8.dp),
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
                                Toast.makeText(context, "Exercício concluído!", Toast.LENGTH_LONG).show()
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

        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = if (mIdExercicio == null) "Criar exercício" else "Editar exercício") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Voltar")
                    }
                }, actions = {
                    mIdExercicio?.let {
                        IconButton(onClick = {
                            mIdExercicio = null
                            viewModel.excluiExercicio(it)
                            Toast.makeText(context, "Exercício excluído!", Toast.LENGTH_LONG).show()
                            navController.popBackStack()
                        }) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Excluir exercício")
                        }
                    }
                }
            )
        }, content = { paddingValues ->
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                        .weight(1f)
                ) {
                    FormItem(
                        value = viewModel.nomeExercicio,
                        label = "Nome do exercício",
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                        onValueChange = {
                            viewModel.nomeExercicio = it
                            viewModel.validaBotao()
                        }
                    )
                    FormItem(
                        value = viewModel.numSeries,
                        label = "Número de séries",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        onValueChange = {
                            viewModel.numSeries = it
                            viewModel.validaBotao()
                        }
                    )
                    FormItem(
                        value = viewModel.minRepeticoes,
                        label = "Mín. repetições",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        onValueChange = {
                            viewModel.minRepeticoes = it
                            viewModel.validaBotao()
                        }
                    )
                    FormItem(
                        value = viewModel.maxRepeticoes,
                        label = "Máx. repetições",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        onValueChange = {
                            viewModel.maxRepeticoes = it
                            viewModel.validaBotao()
                        }
                    )
                    FormItem(
                        value = viewModel.carga,
                        label = "Carga",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        onValueChange = {
                            viewModel.carga = it
                            viewModel.validaBotao()
                        }
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClick = {
                            viewModel.salvaExercicio(idGrupo = idGrupo)
                            Toast.makeText(context, if (idExercicio == null) "Exercício cadastrado!" else "Exercício atualizado!", Toast.LENGTH_LONG).show()
                            navController.popBackStack()
                        }, enabled = viewModel.podeContinuar) {
                        Text(text = "Salvar exercício")
                    }
                }
            }
        })
    }

    @Composable
    fun FormItem(value: String,
                 label: String,
                 keyboardOptions: KeyboardOptions,
                 onValueChange: (String) -> Unit) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            label = { Text(text = label) },
            keyboardOptions = keyboardOptions
        )
    }
}
