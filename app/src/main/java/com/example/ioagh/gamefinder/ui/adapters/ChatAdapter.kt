package com.example.ioagh.gamefinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ioagh.gamefinder.R.*
import com.example.ioagh.gamefinder.models.ChatViewModel
import com.example.ioagh.gamefinder.models.Game
import com.example.ioagh.gamefinder.models.GameViewModel
import kotlinx.android.synthetic.main.chat_list_item.view.*
import kotlinx.android.synthetic.main.game_list_item.view.*

class ChatAdapter(private val chats: ArrayList<ChatViewModel>, private val ctx: Context): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var onItemClick: ((ChatViewModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.chat_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        holder.itemView.chatName.text = chat.gameName + " " + chat.gameDate
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(chats[adapterPosition])
            }
        }
    }
}