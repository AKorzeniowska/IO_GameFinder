package com.example.ioagh.gamefinder.ui.adapters

import android.R.layout
import android.R.id
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ioagh.gamefinder.R
import com.example.ioagh.gamefinder.models.Message
import java.util.*


class MessageAdapter(var context: Context) : BaseAdapter() {
    private var messages: MutableList<Message> = ArrayList<Message>()
    fun add(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(i: Int): Any {
        return messages[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(
        i: Int,
        convertView: View,
        viewGroup: ViewGroup
    ): View {
        var convertView = convertView
        val holder = MessageViewHolder()
        val messageInflater =
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val message: Message = messages[i]
        if (message.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null)
            holder.messageBody =
                convertView.findViewById<View>(R.id.message_body) as TextView
            convertView.tag = holder
            holder.messageBody?.text = message.getText()
        } else {
            convertView = messageInflater.inflate(R.layout.their_message, null)
            holder.avatar =
                convertView.findViewById(R.id.avatar) as View
            holder.name = convertView.findViewById<View>(R.id.name) as TextView
            holder.messageBody =
                convertView.findViewById<View>(R.id.message_body) as TextView
            convertView.tag = holder
            holder.name?.text = message.getMemberData().name
            holder.messageBody?.text = message.getText()
            val drawable = holder.avatar!!.background as GradientDrawable
            drawable.setColor(Color.parseColor(message.getMemberData().color))
        }
        return convertView
    }

}

internal class MessageViewHolder {
    var avatar: View? = null
    var name: TextView? = null
    var messageBody: TextView? = null
}