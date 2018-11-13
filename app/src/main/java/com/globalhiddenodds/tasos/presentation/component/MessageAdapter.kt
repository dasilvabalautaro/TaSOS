package com.globalhiddenodds.tasos.presentation.component

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.inflate
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import kotlinx.android.synthetic.main.view_row_contact.view.*
import kotlinx.android.synthetic.main.view_row_message.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class MessageAdapter @Inject constructor():
        RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    internal var collection: List<MessageView> by Delegates.observable(emptyList()) {
        _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (MessageView,
                                 Navigator.Extras) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MessageAdapter.ViewHolder(parent.inflate(R.layout.view_row_message))

    override fun onBindViewHolder(viewHolder: MessageAdapter.ViewHolder,
                                  position: Int) =
            viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messageView: MessageView, clickListener: (MessageView, Navigator.Extras) -> Unit) {
            itemView.tv_message.text = messageView.message
            itemView.setOnClickListener {
                clickListener(messageView,
                        Navigator.Extras(itemView.tv_message))

            }
        }
    }

}