package br.com.jogosecm.deolhonascores.telas

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import br.com.jogosecm.deolhonascores.R
import br.com.jogosecm.deolhonascores.TelaDoApp
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
        .width(100.dp)


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
                            textStyle = TextStyle.Default.copy(
                                textAlign = TextAlign.Center, fontSize = 40.sp
                            )
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
                            textStyle = TextStyle.Default.copy(
                                textAlign = TextAlign.Center, fontSize = 40.sp
                            )
                        )
                        TextField(
                            value = appUiState.duracaoCor,
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
                            textStyle = TextStyle.Default.copy(
                                textAlign = TextAlign.Center, fontSize = 40.sp
                            )
                        )
                    }
                    Row(
                        modifier = modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = appUiState.palavraColorida,
                            onCheckedChange = { viewModelAtual.mudaSwitchCor(it) },
                            modifier = modifier
                        )
                        Spacer(modifier = modifier.size(10.dp))
                        Text(text = "Palavras coloridas")
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
                                    "\uD83C\uDFA8 Jogo \"Desafio das Cores\" \uD83C\uDFAE\n" +
                                            "\n" +
                                            "Como Jogar\n" +
                                            "Fique atento às cores na tela! A cada contagem regressiva (de 5 a 1, configurável), o comando \"VAI\" será exibido, seguido por uma cor. O desafio é seguir as regras definidas antes do jogo:\n" +
                                            "\n" +
                                            "✅ Tocar ou pegar o objeto correspondente\n" +
                                            "✅ Deslocar-se na direção da cor projetada\n" +
                                            "\n" +
                                            "Após cada ação, um novo estímulo será apresentado, e o jogador deve repetir o processo até o fim do jogo.\n" +
                                            "\n" +
                                            "Objetivos do Jogo\n" +
                                            "\uD83E\uDDE0 Melhorar a atenção e o controle inibitório\n" +
                                            "⚡\uFE0F Desenvolver agilidade e tempo de resposta\n" +
                                            "\uD83D\uDD04 Estimular a flexibilidade cognitiva\n" +
                                            "\n" +
                                            "Prepare-se para um desafio envolvente e dinâmico! \uD83D\uDE80"
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