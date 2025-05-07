
package com.example.amphibians.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.amphibians.data.AmphibiansRepository
import com.example.amphibians.data.NetworkAmphibiansRepository
import com.example.amphibians.network.AmphibianApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException

class AmphibianViewModel(private val amphibiansRepository: AmphibiansRepository) : ViewModel() {


    private val _uiState = MutableStateFlow<AmphibianUiState>(AmphibianUiState.Loading)

    val uiState: StateFlow<AmphibianUiState> = _uiState.asStateFlow()

    init {
        getAmphibiansList()
    }

    /**
     * Fetches the list of amphibians from the repository and updates the UI state.
     * Uses viewModelScope to launch a coroutine for this background operation.
     */
    fun getAmphibiansList() {
        viewModelScope.launch {
            _uiState.value = AmphibianUiState.Loading
            try {

                val amphibians = amphibiansRepository.getAmphibians()

                _uiState.value = AmphibianUiState.Success(amphibians)
            } catch (e: IOException) {

                _uiState.value = AmphibianUiState.Error
            } catch (e: HttpException) {

                _uiState.value = AmphibianUiState.Error
            }
        }
    }

    /**
     * Companion object to provide a ViewModelProvider.Factory for creating instances
     * of AmphibianViewModel, especially when constructor arguments are needed.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val repository = NetworkAmphibiansRepository(AmphibianApi.retrofitService)
                AmphibianViewModel(amphibiansRepository = repository)
            }
        }
    }
}