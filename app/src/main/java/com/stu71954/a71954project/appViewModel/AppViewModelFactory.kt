package com.stu71954.a71954project.appViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.stu71954.a71954project.appRepository.AppRepository

class AppViewModelFactory(private val repository: AppRepository, private val navController: NavController) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository, navController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}