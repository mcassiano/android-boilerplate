package me.cassiano.boilerplate.features.feature

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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

    @Inject lateinit var viewModelFactory: EntityViewModelFactory

    private val adapter = EntityAdapter()
    private val intents = PublishSubject.create<FeatureIntents>()
    private lateinit var swipeLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.recyclerView).run {
            layoutManager = LinearLayoutManager(this@FeatureActivity)
            adapter = this@FeatureActivity.adapter
        }

        swipeLayout = findViewById(R.id.swipeLayout)

        val viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(EntityViewModel::class.java)

        viewModel.processIntents(mergeIntents())

        viewModel.state().observe(this, Observer {
            render(it!!)
        })
    }

    private fun mergeIntents(): Observable<FeatureIntents> {
        return intents.startWith(FeatureIntents.InitialLoadIntent())
                .mergeWith(pullToRefreshIntent())

    }

    private fun pullToRefreshIntent(): Observable<FeatureIntents> {
        return RxSwipeRefreshLayout
                .refreshes(swipeLayout)
                .map { FeatureIntents.PullToRefreshIntent() }
    }

    private fun render(state: EntityViewState) {
        swipeLayout.isRefreshing = state.loading

        state.data?.let {
            adapter.data = DataUpdate(it)
        }
    }
}