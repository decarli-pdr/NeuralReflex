package br.com.jogosecm.neuralreflex.telas

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import br.com.jogosecm.neuralreflex.AppViewModel
import br.com.jogosecm.neuralreflex.R
import br.com.jogosecm.neuralreflex.TelaDoApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuracoes")
val ativado = booleanPreferencesKey("ativado")
fun verificaAtivado(contexto: Context): Flow<Boolean> = contexto.dataStore.data
    .map { configuracoes ->
        configuracoes[ativado] == true
    }

@Composable
fun TelaInicio(
    modifier: Modifier,
    viewModelAtual: AppViewModel,
    mudouOTempoMax: (String) -> Unit,
    mudaramRodadas: (String) -> Unit,
    navHostController: NavHostController,
    contexto: Context,
    mudouTextoValido: (Boolean) -> Unit
) {
    val appUiState by viewModelAtual.uiState.collectAsState()
    var teclado = ImeAction.Done
    val focusManager = LocalFocusManager.current
    val modificadorCaixasTxt = modifier
        .padding(5.dp)
        .height(70.dp)
        .width(110.dp)

    val textStyleCaixas = TextStyle.Default.copy(
        textAlign = TextAlign.Center,
        fontSize = 35.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal
    )

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(key1 = Unit) {
        systemUiController.isSystemBarsVisible = false // Hides both status bar and navigation bar
    }

    var mostrarInfo by remember { mutableStateOf(false) }
    val ativacao by verificaAtivado(contexto).collectAsState(false)

    if (ativacao) {
        Surface(modifier = modifier.fillMaxSize()) {
            Row {
                Column(
                    modifier = modifier
                        .weight(5f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = modifier.padding(20.dp)
                    ) {
                        TextField(
                            value = appUiState.tempoMax,
                            modifier = modificadorCaixasTxt,
                            singleLine = true,
                            onValueChange = {
                                mudouOTempoMax(it)
                                if (it.toIntOrNull() != null && it.toIntOrNull()!! >= 0) {

                                    mudouTextoValido(true)
                                    teclado = ImeAction.Done
                                } else {
                                    mudouTextoValido(false)
                                    teclado = ImeAction.None

                                }
                            },
                            label = {
                                Text(text = "Contagem", textAlign = TextAlign.Center)
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number, imeAction = teclado
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                teclado = ImeAction.Done
                                focusManager.clearFocus()
                            }),
                            textStyle = textStyleCaixas
                        )
                        TextField(
                            value = appUiState.rodadas,
                            modifier = modificadorCaixasTxt,
                            singleLine = true,
                            onValueChange = {
                                mudaramRodadas(it)
                                if (it.toIntOrNull() != null && it.toIntOrNull()!! > 0) {

                                    mudouTextoValido(true)
                                    teclado = ImeAction.Done
                                } else {
                                    mudouTextoValido(false)
                                    teclado = ImeAction.None

                                }
                            },
                            label = {
                                Text(text = "Rodadas", textAlign = TextAlign.Center)
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number, imeAction = teclado
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                teclado = ImeAction.Done
                                focusManager.clearFocus()
                            }),
                            textStyle = textStyleCaixas
                        )
                        TextField(
                            value = appUiState.duracaoImagem,
                            modifier = modificadorCaixasTxt,
                            singleLine = true,
                            onValueChange = {
                                viewModelAtual.mudaDuracaoCor(it)
                                if (it.toIntOrNull() != null && it.toIntOrNull()!! > 0) {

                                    mudouTextoValido(true)
                                    teclado = ImeAction.Done
                                } else {
                                    mudouTextoValido(false)
                                    teclado = ImeAction.None

                                }
                            },
                            label = {
                                Text(text = "Duração", textAlign = TextAlign.Center)
                            },
                            suffix = {
                                Box(
                                    modifier = Modifier.height(35.dp),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    Text(text = "s")
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number, imeAction = teclado
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                teclado = ImeAction.Done
                                focusManager.clearFocus()
                            }),
                            textStyle = textStyleCaixas
                        )
                    }
                    Column {
                        Row(
                            modifier = modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = appUiState.maoDireitaAtivada,
                                onCheckedChange = {
                                    viewModelAtual.mudaMaoDireita(it)
                                    if (it == false && appUiState.maoEsquerdaAtivada == false) {
                                        viewModelAtual.mudaMaoEsquerda(true)
                                    }
                                },
                                modifier = modifier
                            )
                            Spacer(modifier = modifier.width(10.dp))
                            Text(text = "Mão direita")
                        }
                        Row(
                            modifier = modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = appUiState.maoEsquerdaAtivada,
                                onCheckedChange = {
                                    viewModelAtual.mudaMaoEsquerda(it)
                                    if (it == false && appUiState.maoDireitaAtivada == false) {
                                        viewModelAtual.mudaMaoDireita(true)
                                    }
                                },
                                modifier = modifier
                            )
                            Spacer(modifier = modifier.width(10.dp))
                            Text(text = "Mão esquerda")
                        }
                    }
                    Button(
                        modifier = modifier.padding(20.dp),
                        onClick = { navHostController.navigate("Jogo") },
                        enabled = appUiState.estadoBotao
                    ) {
                        Text(
                            "Iniciar", modifier = modifier.padding(10.dp), fontSize = 30.sp
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(5f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    AppLogo(modifier = Modifier.size(130.dp), cor = Color.White)
                    Spacer(modifier = Modifier.size(50.dp)) // Push logo to the bottom
                    Button(
                        modifier = modifier,
                        onClick = { mostrarInfo = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(
                            "Informações", modifier = modifier.padding(10.dp), fontSize = 15.sp
                        )
                    }
                }
            }

            if (mostrarInfo) {
                CaixaDeInformacoes(
                    onDismissRequest = { mostrarInfo = false },
                    content = {
                        Row(
                            Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    "Informações",
                                    modifier = modifier.padding(horizontal = 20.dp)
                                )
                                /* Button(
                                     modifier = Modifier.padding(horizontal = 15.dp),
                                     content = {
                                         Icon(Icons.Rounded.Share, "Compartilhar")
                                     },
                                     enabled = false,
                                     onClick = {
                                         //                                var textoPodios = "**"
                                         //                                podios.forEach { categoria ->
                                         //                                    textoPodios += "\n\n_*${categoria.nomeCategoria}*_"
                                         //                                    categoria.atletas.forEach {
                                         //                                        textoPodios += "\n${it.posicao}\t-\t${it.nome}\t-\t${it.tempo}"
                                         //                                    }
                                         //                                }
                                         //                                val sendIntent: Intent = Intent().apply {
                                         //                                    action = Intent.ACTION_SEND
                                         //                                    putExtra(Intent.EXTRA_TEXT, textoPodios)
                                         //                                    type = "text/plain"
                                         //                                }
                                         //                                val shareIntent =
                                         //                                    Intent.createChooser(sendIntent, "Compartilhar Pódios")
                                         //                                startActivity(contexto, shareIntent, null)
                                     }) */
                            }
                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                    //.width(2000.dp)
                                    .verticalScroll(rememberScrollState())
                                // Adjust height as needed
                                //.heightIn(max = 300.dp)

                            ) {
                                Text(
                                    "Este jogo é protegido por direitos autorais. Não pode ser modificado, copiado, distribuído ou comercializado sem a autorização expressa do criador. Todos os direitos reservados.\n" +
                                            "\n" +
                                            "Criador: Anderson Amaral\n" +
                                            "Mestre em Saúde e Tecnologia no Espaço Hospitalar – Pós-graduação em Neuropsicologia Reab Cognitva e Pós-graduação em Telessaúde\n"
                                )
                                Text(
                                    "\uD83C\uDFA8 NeuroReflex \uD83C\uDFAE\n" +
                                            "\n" +
                                            "Como Jogar:\n" +
                                            "Fique sempre atento às mãos que surgem na tela! Você pode escolher se quer trabalhar a mão direita, a esquerda, ou, para um desafio ainda maior, ambas as mãos. A cada contagem regressiva (configurável de 5 a 1), o comando \"VAI\" aparece, acompanhado de uma mão mostrando um número com os dedos. O objetivo é reproduzir, com sua própria mão, a mesma quantidade de dedos, e identificar qual delas – direita ou esquerda – será a sua referência.\n" +
                                            "Cada ação concluída traz um novo estímulo, e você deve repetir o processo até alcançar o final do jogo.\n" +
                                            "\n" +
                                            "Objetivos do Jogo: \n\uD83E\uDDE0 Atenção e Controle Inibitório: Desenvolva sua capacidade de concentração e autocontrole em situações de alta demanda.\n" +
                                            "⚡\uFE0F Agilidade e Tempo de Resposta: Aperfeiçoe sua rapidez e precisão com cada novo desafio.\n" +
                                            "\uD83D\uDD04 Flexibilidade Cognitiva: Estimule sua capacidade de adaptação e resposta a diferentes estímulos.\n" +
                                            "✋ Motricidade Fina: Melhore o controle e a destreza dos movimentos das mãos.\n" +
                                            "↔\uFE0F Lateralidade: Fortaleça o reconhecimento e a coordenação entre as mãos esquerda e direita.\n" +
                                            "\uD83D\uDD22 Valores Numéricos: Desenvolva a percepção de números de forma interativa e desafiadora.\n" +
                                            "\n" +
                                            "Prepare-se para um jogo envolvente e dinâmico que vai desafiar sua mente e corpo! \uD83D\uDE80"
                                )
                                Image(
                                    painter = painterResource(R.drawable.capa),
                                    contentDescription = "Logomarca dos jogos",
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                )

            }
        }
    } else {
        TelaAtivacao(contexto = contexto)
    }


}

@Composable
fun CaixaDeInformacoes(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // Custom layout to control width
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            content()
        }
    }
}

@Preview(
    showSystemUi = true, showBackground = true, device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun TelaInicioPreview() {
    TelaDoApp(
        modifier = Modifier
    )
}

@Preview(
    showSystemUi = true, showBackground = true, device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun CaixaPreview() {
    CaixaDeInformacoes(
        onDismissRequest = { },
        content = {
            Text("Teste")
        }
    )
}