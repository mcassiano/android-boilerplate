package me.cassiano.boilerplate.features.feature.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import me.cassiano.boilerplate.data.db.entity.Entity
import me.cassiano.boilerplate.util.AutoUpdatableAdapter
import kotlin.properties.Delegates

class EntityAdapter : RecyclerView.Adapter<ViewHolder>(), AutoUpdatableAdapter {

    var data: DataUpdate by Delegates.observable(DataUpdate(entities = emptyList())) { _, old, new ->
        autoNotify(old.entities, new.entities) { _, _ -> old == new }
    }

    override fun getItemCount(): Int = data.entities.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = TextView(parent?.context).apply {
            textSize = 50f
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(data.entities[position])
    }

}

data class DataUpdate(val entities: List<Entity>)