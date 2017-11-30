package me.cassiano.boilerplate.features.feature.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import me.cassiano.boilerplate.data.EntityRepositoryContract
import me.cassiano.boilerplate.features.feature.intents.FeatureIntents
import io.reactivex.ObservableTransformer


class EntityViewModel(private val repository: EntityRepositoryContract) : ViewModel() {

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
                .doOnNext({ Log.d("New intent", it.toString()) })
                .switchMap {
                    when (it) {
                        is FeatureIntents.InitialLoadIntent -> repository.getEntities()
                        is FeatureIntents.PullToRefreshIntent -> repository.fetchEntities()
                    }.toObservable()
                            .map { EntityViewState.success(it) }
                            .startWith(EntityViewState.loading())
                            .subscribeOn(Schedulers.computation())
                            .onErrorReturn { EntityViewState.error(it) }
                }
                .doOnNext({ Log.d("New state", it.toString()) })
                .doOnSubscribe({ Log.d("New state", "New View model started") })
                .doOnDispose({ Log.d("New state", "View model stopped") })
                .subscribe { state.postValue(it) })
    }
}