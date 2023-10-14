package com.dicoding.githubuser.mode

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ModeViewModel(private val pref: SettingPreferences) : ViewModel() {

        fun getThemeSettings(): LiveData<Boolean> {
            return pref.getThemeSetting().asLiveData()
        }

        fun saveThemeSetting(isDarkModeActive: Boolean) {
            viewModelScope.launch {
                pref.saveThemeSetting(isDarkModeActive)
            }
        }
    class ViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ModeViewModel::class.java)) {
                return ModeViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}

