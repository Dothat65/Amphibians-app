package com.example.amphibians.ui

import com.example.amphibians.data.Amphibian

sealed interface AmphibianUiState {
    /**
     * Represents the successful state where amphibian data has been loaded.
     * @property amphibians The list of loaded amphibian data.
     */
    data class Success(val amphibians: List<Amphibian>) : AmphibianUiState


    object Error : AmphibianUiState


    object Loading : AmphibianUiState
}