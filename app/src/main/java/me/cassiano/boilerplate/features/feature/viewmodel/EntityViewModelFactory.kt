package me.cassiano.boilerplate.features.feature.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.Scheduler
import me.cassiano.boilerplate.data.EntityRepositoryContract
import me.cassiano.boilerplate.injection.PerActivity
import me.cassiano.boilerplate.injection.qualifiers.ComputationScheduler
import javax.inject.Inject

@PerActivity
class EntityViewModelFactory @Inject constructor(
        private val repository: EntityRepositoryContract,
        @ComputationScheduler private val computationScheduler: Scheduler) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EntityViewModel(repository, computationScheduler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}