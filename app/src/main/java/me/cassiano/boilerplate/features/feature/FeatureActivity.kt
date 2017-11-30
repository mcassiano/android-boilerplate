package me.cassiano.boilerplate.features.feature

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.cassiano.boilerplate.BaseActivity
import me.cassiano.boilerplate.R
import me.cassiano.boilerplate.features.feature.adapter.DataUpdate
import me.cassiano.boilerplate.features.feature.adapter.EntityAdapter
import me.cassiano.boilerplate.features.feature.intents.FeatureIntents
import me.cassiano.boilerplate.features.feature.viewmodel.EntityViewModel
import me.cassiano.boilerplate.features.feature.viewmodel.EntityViewModelFactory
import me.cassiano.boilerplate.features.feature.viewmodel.EntityViewState
import javax.inject.Inject

class FeatureActivity : BaseActivity() {

    @BindView(R.id.swipeLayout) lateinit var swipeLayout: SwipeRefreshLayout
    @BindView(R.id.recyclerView) lateinit var names: RecyclerView
    @Inject lateinit var viewModelFactory: EntityViewModelFactory

    private val adapter = EntityAdapter()
    private val intents = PublishSubject.create<FeatureIntents>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        with(names) {
            layoutManager = LinearLayoutManager(this@FeatureActivity)
            adapter = this@FeatureActivity.adapter
        }

        val viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(EntityViewModel::class.java)
                .also { it.processIntents(mergeIntents()) }

        viewModel.state().observe(this, Observer { render(it!!) })
    }

    private fun render(state: EntityViewState) {
        swipeLayout.isRefreshing = state.loading

        state.data?.let {
            adapter.data = DataUpdate(it)
        }
    }

    private fun mergeIntents(): Observable<FeatureIntents> {
        return intents.startWith(initialLoadIntent())
                .mergeWith(pullToRefreshIntent())

    }

    private fun initialLoadIntent(): Observable<FeatureIntents> =
            Observable.just(FeatureIntents.InitialLoadIntent())

    private fun pullToRefreshIntent(): Observable<FeatureIntents> =
            RxSwipeRefreshLayout
                    .refreshes(swipeLayout)
                    .map { FeatureIntents.PullToRefreshIntent() }
}