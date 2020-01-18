package com.example.ioagh.gamefinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.GameViewModel
import kotlinx.android.synthetic.main.game_list_item.view.*

class ChooseGameAdapter(private val gameModels: ArrayList<GameViewModel>, private val ctx: Context): RecyclerView.Adapter<ChooseGameAdapter.ViewHolder>() {

    var onItemClick: ((GameViewModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.game_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gameModels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameModel = gameModels[position]
        holder.itemView.gameName.text = gameModel.game.gameName
        holder.itemView.distance.text = gameModel.game.date
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(gameModels[adapterPosition])
            }
        }
    }
}