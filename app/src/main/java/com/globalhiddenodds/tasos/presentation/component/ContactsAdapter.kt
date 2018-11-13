package com.globalhiddenodds.tasos.presentation.component

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.inflate
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import kotlinx.android.synthetic.main.view_row_contact.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class ContactsAdapter @Inject constructor():
        RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    internal var collection: List<GroupMessageView> by Delegates.observable(emptyList()) {
        _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (GroupMessageView, Navigator.Extras) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ContactsAdapter.ViewHolder(parent.inflate(R.layout.view_row_contact))

    override fun onBindViewHolder(viewHolder: ContactsAdapter.ViewHolder, position: Int) =
            viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(groupView: GroupMessageView, clickListener: (GroupMessageView, Navigator.Extras) -> Unit) {
            itemView.tv_name.text = groupView.source
            itemView.tv_alert.text = groupView.quantity.toString()
            itemView.setOnClickListener {
                clickListener(groupView,
                        Navigator.Extras(itemView.tv_name))

            }
        }
    }

}