package me.cassiano.boilerplate.features.feature.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import me.cassiano.boilerplate.data.EntityRepositoryContract
import me.cassiano.boilerplate.data.db.entity.Entity
import me.cassiano.boilerplate.features.feature.intents.FeatureIntents
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class EntityViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var viewModel: EntityViewModel
    private lateinit var repo: EntityRepositoryContract
    private lateinit var intents: PublishSubject<FeatureIntents>
    private lateinit var observer: Observer<EntityViewState>

    @Before
    fun setUp() {
        repo = mock()
        observer = mock()
        viewModel = EntityViewModel(repo, Schedulers.trampoline())
        intents = PublishSubject.create()
    }

    @Test
    fun viewModelShouldEmitInitialStateOnlyOnceForInitialLoadIntent() {

        val characters = listOf(
                Entity("1", "Harry"),
                Entity("2", "Hermione")
        )

        whenever(repo.getEntities()).thenReturn(Single.just(characters))
        whenever(repo.fetchEntities()).thenReturn(Single.just(characters))
        viewModel.processIntents(intents)
        viewModel.state().observeForever(observer)

        intents.onNext(FeatureIntents.InitialLoadIntent())
        intents.onNext(FeatureIntents.InitialLoadIntent())

        verify(observer, times(1)).onChanged(EntityViewState.loading())
        verify(observer, times(1)).onChanged(EntityViewState.success(characters))
    }

    @Test
    fun viewModelShouldEmitInitialStateAndUpdateWhenPullToRefreshTriggered() {

        val firstChars = listOf(
                Entity("1", "Harry"),
                Entity("2", "Hermione")
        )

        val updatedChars = firstChars + listOf(Entity("3", "Ron"))

        whenever(repo.getEntities()).thenReturn(Single.just(firstChars))
        whenever(repo.fetchEntities()).thenReturn(Single.just(updatedChars))

        viewModel.processIntents(intents)
        viewModel.state().observeForever(observer)

        intents.onNext(FeatureIntents.InitialLoadIntent())
        intents.onNext(FeatureIntents.PullToRefreshIntent())

        verify(observer, times(2)).onChanged(EntityViewState.loading())
        verify(observer, times(1)).onChanged(EntityViewState.success(firstChars))
        verify(observer, times(1)).onChanged(EntityViewState.success(updatedChars))
    }

}