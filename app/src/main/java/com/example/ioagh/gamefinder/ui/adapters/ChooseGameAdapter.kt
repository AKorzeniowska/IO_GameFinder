package com.example.ioagh.gamefinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ioagh.gamefinder.R.*
import kotlinx.android.synthetic.main.game_list_item.view.*

class ChooseGameAdapter(private val myDataset: Array<String>,private val ctx: Context): RecyclerView.Adapter<ChooseGameAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.game_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.gameName.text = myDataset[position]
    }
}