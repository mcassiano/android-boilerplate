package me.cassiano.boilerplate.features.feature

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import dagger.android.AndroidInjection
import me.cassiano.boilerplate.BaseActivity
import me.cassiano.boilerplate.R
import me.cassiano.boilerplate.features.feature.adapter.DataUpdate
import me.cassiano.boilerplate.features.feature.adapter.EntityAdapter
import me.cassiano.boilerplate.features.feature.viewmodel.EntityViewModel
import me.cassiano.boilerplate.features.feature.viewmodel.EntityViewModelFactory
import me.cassiano.boilerplate.features.feature.viewmodel.EntityViewState
import javax.inject.Inject

class FeatureActivity : BaseActivity() {

    @Inject lateinit var viewModelFactory: EntityViewModelFactory

    private val adapter = EntityAdapter()
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

        viewModel.state().observe(this, Observer {
            render(it!!)
        })

        swipeLayout.setOnRefreshListener { viewModel.fetchEntities() }
        viewModel.getEntities()

    }

    private fun render(state: EntityViewState) {
        state.data?.let {
            swipeLayout.isRefreshing = false
            adapter.data = DataUpdate(it)
        }
    }
}