
package com.example.amphibians.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.amphibians.R // You'll need to create some string resources
import com.example.amphibians.data.Amphibian

/**
 * Main Composable for the Amphibians app.
 * It observes the UI state from the ViewModel and displays the appropriate screen.
 */
@Composable
fun AmphibianApp(
    modifier: Modifier = Modifier,

    amphibianViewModel: AmphibianViewModel = viewModel(factory = AmphibianViewModel.Factory)
) {

    val amphibianUiState by amphibianViewModel.uiState.collectAsState()


    when (val currentState = amphibianUiState) {
        is AmphibianUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is AmphibianUiState.Success -> AmphibianListScreen(
            amphibians = currentState.amphibians,
            modifier = modifier.fillMaxSize(),

            onRetry = { amphibianViewModel.getAmphibiansList() }
        )
        is AmphibianUiState.Error -> ErrorScreen(

            onRetry = { amphibianViewModel.getAmphibiansList() },
            modifier = modifier.fillMaxSize()
        )
    }
}

/**
 * Composable function to display a list of amphibians.
 * @param amphibians The list of amphibian data to display.
 * @param modifier Modifier for this composable.
 * @param onRetry Callback function to be invoked when the retry action is triggered.
 */
@Composable
fun AmphibianListScreen(
    amphibians: List<Amphibian>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    if (amphibians.isEmpty()) {

        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.no_amphibians_found),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry_button))
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items
    ) {
        items(amphibians) { amphibian ->
            AmphibianCard(amphibian = amphibian, modifier = Modifier.fillMaxWidth())
        }
    }
}

/**
 * Composable function to display a single amphibian's details in a card.
 * @param amphibian The amphibian data to display.
 * @param modifier Modifier for this composable.
 */
@Composable
fun AmphibianCard(amphibian: Amphibian, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Shadow effect
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "${amphibian.name} (${amphibian.type})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Display Image using Coil's AsyncImage
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(amphibian.imgSrc)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.amphibian_image_description, amphibian.name),
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                error = painterResource(id = android.R.drawable.ic_menu_report_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = amphibian.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }
    }
}

/**
 * Composable function to display a loading indicator.
 * @param modifier Modifier for this composable.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.loading_amphibians),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

/**
 * Composable function to display an error message and a retry button.
 * @param onRetry Callback function to be invoked when the retry button is clicked.
 * @param modifier Modifier for this composable.
 */
@Composable
fun ErrorScreen(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.loading_failed),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry_button))
        }
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun AmphibianAppLoadingPreview() {
    MaterialTheme {

        LoadingScreen(Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun AmphibianAppErrorPreview() {
    MaterialTheme {
        ErrorScreen(onRetry = {}, modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun AmphibianCardPreview() {
    MaterialTheme {
        val sampleAmphibian = Amphibian(
            name = "Great Crested Newt",
            type = "Newt",
            description = "The great crested newt is the largest native newt species in Great Britain. It has a warty skin, unlike the smooth skin of other newts, and a long, flattened tail.",
            imgSrc = ""
        )
        AmphibianCard(amphibian = sampleAmphibian, modifier = Modifier.padding(8.dp))
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun AmphibianListScreenPreview() {
    MaterialTheme {
        val sampleAmphibians = listOf(
            Amphibian("Frog", "Toad", "A hoppy friend.", ""),
            Amphibian("Salamander", "Amphibian", "A slinky friend.", "")
        )
        AmphibianListScreen(amphibians = sampleAmphibians, onRetry = {}, modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun AmphibianListScreenEmptyPreview() {
    MaterialTheme {
        AmphibianListScreen(amphibians = emptyList(), onRetry = {}, modifier = Modifier.fillMaxSize())
    }
}
