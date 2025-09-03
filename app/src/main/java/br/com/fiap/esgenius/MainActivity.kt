package br.com.fiap.esgenius

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import android.graphics.Paint
import androidx.compose.ui.unit.sp as textSp

// Dados de exemplo para simular os indicadores ESG e o histórico.
data class ESGData(
    val date: String,
    val riskScore: Int,
    val governanceScore: Int,
    val environmentalScore: Int,
    val emissions: String,
    val renewableEnergy: String,
    val wasteManagement: String,
    val sustainabilityProjects: String,
    val diversityPolicies: String,
    val workerSafety: String,
    val communityRelations: String,
    val boardStructure: String,
    val transparency: String,
    val antiCorruption: String
)

data class Company(
    val name: String,
    val logoColor: Color,
    val sector: String,
    val country: String,
    val ticker: String,
    val currentESG: ESGData,
    val history: List<ESGData>
)

val mockCompany = Company(
    name = "ESGenius",
    logoColor = Color.Blue,
    sector = "Tecnologia",
    country = "Brasil",
    ticker = "ESG3",
    currentESG = ESGData(
        date = "03/09/2025",
        riskScore = 25,
        governanceScore = 15,
        environmentalScore = 10,
        emissions = "2500 toneladas CO₂",
        renewableEnergy = "45%",
        wasteManagement = "Certificado ISO 14001",
        sustainabilityProjects = "Projeto de reflorestamento na Amazônia",
        diversityPolicies = "Programa de trainee para minorias",
        workerSafety = "Zero acidentes no último ano",
        communityRelations = "Apoio a escolas locais",
        boardStructure = "50% de membros independentes",
        transparency = "Relatórios trimestrais detalhados",
        antiCorruption = "Política de 'tolerância zero'"
    ),
    history = listOf(
        ESGData("01/2024", 30, 18, 12, "", "", "", "", "", "", "", "", "", ""),
        ESGData("04/2024", 28, 17, 11, "", "", "", "", "", "", "", "", "", ""),
        ESGData("07/2024", 26, 16, 10, "", "", "", "", "", "", "", "", "", ""),
        ESGData("10/2024", 25, 15, 10, "", "", "", "", "", "", "", "", "", "")
    )
)

val mockSectorAverage = listOf(
    ESGData("01/2024", 32, 19, 13, "", "", "", "", "", "", "", "", "", ""),
    ESGData("04/2024", 30, 18, 12, "", "", "", "", "", "", "", "", "", ""),
    ESGData("07/2024", 28, 17, 11, "", "", "", "", "", "", "", "", "", ""),
    ESGData("10/2024", 27, 16, 11, "", "", "", "", "", "", "", "", "", "")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showHistory by remember { mutableStateOf(false) }

                    if (showHistory) {
                        ESGHistoryScreen(
                            company = mockCompany,
                            sectorHistory = mockSectorAverage,
                            onBack = { showHistory = false }
                        )
                    } else {
                        CompanyDetailScreen(
                            company = mockCompany,
                            onShowHistory = { showHistory = true }
                        )
                    }
                }
            }
        }
    }
}

// Tela de Detalhes da Empresa - Indicadores Individuais
@Composable
fun CompanyDetailScreen(company: Company, onShowHistory: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Título da tela com logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(company.logoColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "E", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Detalhes da Empresa",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            // Identificação da Empresa
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo (placeholder)
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(company.logoColor)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = company.name, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Setor: ${company.sector} | País: ${company.country}", fontSize = 14.sp, color = Color.Gray)
                        Text(text = "Código na bolsa: ${company.ticker}", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Cartão para o ranking de sustentabilidade
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ranking de Empresas Sustentáveis",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    val sustainabilityScore = company.currentESG.governanceScore + company.currentESG.environmentalScore
                    Text(
                        text = "Pontuação de Sustentabilidade: ${sustainabilityScore}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Cartão para indicadores individuais Ambientais (E)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ambiental (E - Environmental)",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    IndicatorDetail(label = "Emissões de CO₂", value = company.currentESG.emissions)
                    IndicatorDetail(label = "Uso de energia renovável", value = company.currentESG.renewableEnergy)
                    IndicatorDetail(label = "Gestão de resíduos", value = company.currentESG.wasteManagement)
                    IndicatorDetail(label = "Projetos de sustentabilidade", value = company.currentESG.sustainabilityProjects)
                }
            }
        }

        // Cartão para indicadores individuais Sociais (S)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Social (S - Social)",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    IndicatorDetail(label = "Políticas de diversidade", value = company.currentESG.diversityPolicies)
                    IndicatorDetail(label = "Saúde e segurança", value = company.currentESG.workerSafety)
                    IndicatorDetail(label = "Relacionamento com a comunidade", value = company.currentESG.communityRelations)
                }
            }
        }

        // Cartão para indicadores individuais de Governança (G)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Governança (G - Governance)",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    IndicatorDetail(label = "Estrutura do conselho", value = company.currentESG.boardStructure)
                    IndicatorDetail(label = "Transparência em relatórios", value = company.currentESG.transparency)
                    IndicatorDetail(label = "Políticas anticorrupção", value = company.currentESG.antiCorruption)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botão para navegar para a tela de histórico
        item {
            Button(
                onClick = onShowHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Ver Histórico ESG", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

// Layout para exibir um indicador individual
@Composable
fun IndicatorDetail(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 16.sp)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Gray)
    }
}

// Tela de Histórico ESG - Evolução no tempo
@Composable
fun ESGHistoryScreen(company: Company, sectorHistory: List<ESGData>, onBack: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // Título com o logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(company.logoColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "E", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Histórico ESG - ${company.name}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Período:", fontWeight = FontWeight.SemiBold)
                Button(
                    onClick = { /* Lógica de filtro */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                ) {
                    Text("Últimos 4 Trimestres", fontSize = 12.sp)
                }
                Button(
                    onClick = { /* Lógica de filtro */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                ) {
                    Text("Últimos 5 Anos", fontSize = 12.sp)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Gráfico de Pontuações ESG (Linha)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ESGScoresLineChart(companyHistory = company.history, sectorHistory = mockSectorAverage)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            HighlightsCard(history = company.history)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Comparação Trimestral (Barras)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ESGBarChart(history = company.history)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(company.history) { data ->
            HistoryItem(data)
        }
    }
}

@Composable
fun HighlightsCard(history: List<ESGData>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Destaques",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            var maxIncrease = 0
            var maxDecrease = 0
            var increasePeriod = ""
            var decreasePeriod = ""

            for (i in 1 until history.size) {
                val currentGov = history[i].governanceScore
                val previousGov = history[i-1].governanceScore
                val change = currentGov - previousGov
                if (change > maxIncrease) {
                    maxIncrease = change
                    increasePeriod = "${history[i-1].date} -> ${history[i].date}"
                }
                if (change < maxDecrease) {
                    maxDecrease = change
                    decreasePeriod = "${history[i-1].date} -> ${history[i].date}"
                }
            }

            if (maxIncrease > 0) {
                Text("Maior crescimento em Governança: +$maxIncrease pontos ($increasePeriod)",
                    color = Color.Green, fontSize = 14.sp)
            }
            if (maxDecrease < 0) {
                Text("Maior queda em Governança: $maxDecrease pontos ($decreasePeriod)",
                    color = Color.Red, fontSize = 14.sp)
            }
        }
    }
}

// Composable que desenha o gráfico de pontuações ESG (Linhas)
@Composable
fun ESGScoresLineChart(companyHistory: List<ESGData>, sectorHistory: List<ESGData>) {
    val textMeasurer = rememberTextMeasurer()
    val maxScore = 100f
    val paddingHorizontal = 32.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(modifier = Modifier.weight(1f).fillMaxWidth()) {
                val totalWidth = size.width - paddingHorizontal.toPx() * 2
                val totalHeight = size.height - 40.dp.toPx()
                val pointCount = companyHistory.size
                val xStep = if (pointCount > 1) totalWidth / (pointCount - 1) else 0f

                val companyPathGov = Path()
                val companyPathEnv = Path()
                val sectorPathGov = Path()
                val sectorPathEnv = Path()

                companyHistory.forEachIndexed { index, data ->
                    val x = paddingHorizontal.toPx() + (index * xStep)

                    val governanceY = totalHeight * (1 - (data.governanceScore / maxScore)) + 20.dp.toPx()
                    val environmentalY = totalHeight * (1 - (data.environmentalScore / maxScore)) + 20.dp.toPx()
                    val sectorGovY = totalHeight * (1 - (sectorHistory[index].governanceScore / maxScore)) + 20.dp.toPx()
                    val sectorEnvY = totalHeight * (1 - (sectorHistory[index].environmentalScore / maxScore)) + 20.dp.toPx()

                    if (index == 0) {
                        companyPathGov.moveTo(x, governanceY)
                        companyPathEnv.moveTo(x, environmentalY)
                        sectorPathGov.moveTo(x, sectorGovY)
                        sectorPathEnv.moveTo(x, sectorEnvY)
                    } else {
                        companyPathGov.lineTo(x, governanceY)
                        companyPathEnv.lineTo(x, environmentalY)
                        sectorPathGov.lineTo(x, sectorGovY)
                        sectorPathEnv.lineTo(x, sectorEnvY)
                    }

                    drawCircle(color = Color.Red, radius = 4.dp.toPx(), center = Offset(x, governanceY))
                    drawCircle(color = Color.Green, radius = 4.dp.toPx(), center = Offset(x, environmentalY))
                    drawCircle(color = Color.Gray, radius = 4.dp.toPx(), center = Offset(x, sectorGovY))
                    drawCircle(color = Color.DarkGray, radius = 4.dp.toPx(), center = Offset(x, sectorEnvY))

                    drawText(
                        textMeasurer = textMeasurer,
                        text = data.date,
                        topLeft = Offset(x - 20.dp.toPx(), size.height - 20.dp.toPx()),
                        style = TextStyle(fontSize = 10.sp)
                    )
                }

                drawPath(path = companyPathGov, color = Color.Red, style = Stroke(width = 2.dp.toPx()))
                drawPath(path = companyPathEnv, color = Color.Green, style = Stroke(width = 2.dp.toPx()))
                drawPath(path = sectorPathGov, color = Color.Gray, style = Stroke(width = 2.dp.toPx()))
                drawPath(path = sectorPathEnv, color = Color.DarkGray, style = Stroke(width = 2.dp.toPx()))
            }

            // Legenda abaixo do gráfico
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = Color.Red, label = "Governança")
                Spacer(modifier = Modifier.width(8.dp))
                LegendItem(color = Color.Green, label = "Ambiental")
                Spacer(modifier = Modifier.width(8.dp))
                LegendItem(color = Color.Gray, label = "Média Setor (Gov.)")
                Spacer(modifier = Modifier.width(8.dp))
                LegendItem(color = Color.DarkGray, label = "Média Setor (Amb.)")
            }
        }
    }
}

// Composable que desenha o gráfico de barras
@Composable
fun ESGBarChart(history: List<ESGData>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = Color.Red, label = "Governança")
                Spacer(modifier = Modifier.width(8.dp))
                LegendItem(color = Color.Green, label = "Ambiental")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                history.forEach { data ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight().weight(1f)
                    ) {
                        Box(modifier = Modifier.height(data.governanceScore.dp * 2f).width(20.dp).background(Color.Red))
                        Box(modifier = Modifier.height(data.environmentalScore.dp * 2f).width(20.dp).background(Color.Green))
                        Text(text = data.date, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 12.sp)
    }
}

// Layout para exibir um item do histórico
@Composable
fun HistoryItem(data: ESGData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Data: ${data.date}", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Governança: ${data.governanceScore}", fontSize = 14.sp)
            Text(text = "Meio Ambiente: ${data.environmentalScore}", fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyDetailPreview() {
    CompanyDetailScreen(company = mockCompany, onShowHistory = {})
}

@Preview(showBackground = true)
@Composable
fun ESGHistoryPreview() {
    ESGHistoryScreen(company = mockCompany, sectorHistory = mockSectorAverage, onBack = {})
}
