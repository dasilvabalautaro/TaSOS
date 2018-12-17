package com.globalhiddenodds.tasos.presentation.component

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.inflate
import com.globalhiddenodds.tasos.presentation.data.GroupMessageView
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import com.globalhiddenodds.tasos.presentation.presenter.DeleteUserViewModel
import com.globalhiddenodds.tasos.presentation.view.activities.ContactActivity
import com.globalhiddenodds.tasos.presentation.view.fragments.ContactFragment
import kotlinx.android.synthetic.main.view_row_contact.view.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
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


        fun bind(groupView: GroupMessageView, clickListener: (GroupMessageView,
                                                              Navigator.Extras) -> Unit) {
            itemView.tv_name.text = groupView.source
            itemView.tv_alert.text = groupView.quantity.toString()
            itemView.setOnClickListener {
                clickListener(groupView,
                        Navigator.Extras(itemView.tv_name))

            }
            itemView.ib_trash_user.setOnClickListener {
                ContactFragment.deleteUser(itemView.tv_name.text.toString())

            }
        }
    }

}