package me.cassiano.boilerplate.features.feature.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import me.cassiano.boilerplate.data.EntityRepositoryContract
import me.cassiano.boilerplate.injection.PerActivity
import javax.inject.Inject

@PerActivity
class EntityViewModelFactory @Inject constructor(
        private val repository: EntityRepositoryContract) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EntityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}