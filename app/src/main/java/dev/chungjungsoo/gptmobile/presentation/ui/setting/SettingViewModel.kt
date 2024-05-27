package dev.chungjungsoo.gptmobile.presentation.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.chungjungsoo.gptmobile.data.datastore.SettingDataSource
import dev.chungjungsoo.gptmobile.data.dto.Platform
import dev.chungjungsoo.gptmobile.data.model.ApiType
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingDataSource: SettingDataSource
) : ViewModel() {

    private val _platformState = MutableStateFlow(listOf<Platform>())
    val platformState: StateFlow<List<Platform>> = _platformState.asStateFlow()

    private val _isThemeDialogOpen = MutableStateFlow(false)
    val isThemeDialogOpen: StateFlow<Boolean> = _isThemeDialogOpen.asStateFlow()

    init {
        fetchPlatformStatus()
    }

    fun fetchPlatformStatus() {
        viewModelScope.launch {
            val platforms = ApiType.entries.map { apiType ->
                val status = settingDataSource.getStatus(apiType)
                val token = settingDataSource.getToken(apiType)
                val model = settingDataSource.getModel(apiType)

                Platform(apiType, enabled = status ?: false, token = token, model = model)
            }
            _platformState.update { platforms }
        }
    }

    fun openThemeDialog() {
        _isThemeDialogOpen.update { true }
    }

    fun closeThemeDialog() {
        _isThemeDialogOpen.update { false }
    }
}
