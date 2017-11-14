package me.cassiano.boilerplate.features.feature.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import me.cassiano.boilerplate.data.EntityRepositoryContract


class EntityViewModel(private val repository: EntityRepositoryContract) : ViewModel() {

    private val getEntitiesIntent = PublishSubject.create<Boolean>()
    private val fetchEntitiesIntent = PublishSubject.create<Boolean>()
    private val disposables = CompositeDisposable()
    private val state = MutableLiveData<EntityViewState>()

    init {
        disposables.add(getEntitiesIntent.filter { it }
                .switchMap {
                    repository
                            .getEntities()
                            .subscribeOn(Schedulers.io())
                            .toObservable()
                            .map { EntityViewState.success(it) }
                            .startWith(EntityViewState.loading())
                            .onErrorReturn { EntityViewState.error(it) }
                }.subscribe({ state.postValue(it) }, { }))

        disposables.add(fetchEntitiesIntent.filter { it }
                .switchMap {
                    repository.fetchEntities()
                            .subscribeOn(Schedulers.io())
                            .toObservable()
                            .map { EntityViewState.success(it) }
                            .startWith(EntityViewState.loading())
                            .onErrorReturn { EntityViewState.error(it) }
                }.subscribe({ state.postValue(it) }, { }))
    }

    override fun onCleared() {
        disposables.clear()
    }

    fun state(): LiveData<EntityViewState> {
        return state
    }

    fun getEntities() = getEntitiesIntent.onNext(true)
    fun fetchEntities() = fetchEntitiesIntent.onNext(true)
}