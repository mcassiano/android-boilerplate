package me.cassiano.boilerplate.features.feature.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.ButterKnife
import me.cassiano.boilerplate.data.db.entity.Entity

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bind(entity: Entity) {
        (itemView as TextView).text = entity.name
    }

}