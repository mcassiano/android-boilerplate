package me.cassiano.boilerplate.features.feature.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import me.cassiano.boilerplate.data.EntityRepositoryContract
import me.cassiano.boilerplate.features.feature.intents.FeatureIntents


class EntityViewModel(private val repository: EntityRepositoryContract,
                      private var computationScheduler: Scheduler) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val intentsSubject = PublishSubject.create<FeatureIntents>()
    private val intentFilter: ObservableTransformer<FeatureIntents, FeatureIntents> =
            ObservableTransformer { intents ->
                intents.publish({ shared ->
                    Observable.merge<FeatureIntents>(
                            shared.ofType(FeatureIntents.InitialLoadIntent::class.java).take(1),
                            shared.filter({ intent -> intent !is FeatureIntents.InitialLoadIntent })
                    )
                })
            }
    private val state = MutableLiveData<EntityViewState>()

    init {
        setupIntents()
    }

    override fun onCleared() {
        disposables.clear()
    }

    fun state(): LiveData<EntityViewState> = state

    fun processIntents(intents: Observable<FeatureIntents>) {
        intents.subscribe(intentsSubject)
    }

    private fun setupIntents() {
        disposables.add(intentsSubject
                .compose(intentFilter)
                .switchMap {
                    when (it) {
                        is FeatureIntents.InitialLoadIntent -> repository.getEntities()
                        is FeatureIntents.PullToRefreshIntent -> repository.fetchEntities()
                    }.toObservable()
                            .map { EntityViewState.success(it) }
                            .startWith(EntityViewState.loading())
                            .subscribeOn(computationScheduler)
                            .onErrorReturn { EntityViewState.error(it) }
                }
                .onErrorReturn { EntityViewState.error(it) }
                .subscribe { state.postValue(it) })
    }
}