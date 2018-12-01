package com.globalhiddenodds.tasos.presentation.component

import android.support.constraint.ConstraintLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.inflate
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import kotlinx.android.synthetic.main.view_row_message.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.backgroundResource
import javax.inject.Inject
import kotlin.properties.Delegates

class MessageAdapter @Inject constructor():
        RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

   /* internal var collection: List<MessageView> by Delegates.observable(emptyList()) {
        _, _, _ -> notifyDataSetChanged()
    }*/
    internal val collection: ArrayList<MessageView> = ArrayList()

    private var clickListener: (MessageView,
                                 Navigator.Extras) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MessageAdapter.ViewHolder(parent.inflate(R.layout.view_row_message))

    override fun onBindViewHolder(viewHolder: MessageAdapter.ViewHolder,
                                  position: Int) =
            viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    fun setObjectList(itemList: ArrayList<MessageView>){
        val diffResult: DiffUtil.DiffResult = DiffUtil
                .calculateDiff(DataListDiffCallback(this
                        .collection, itemList))
        this.collection.clear()
        this.collection.addAll(itemList)
        diffResult.dispatchUpdatesTo(this)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messageView: MessageView, clickListener: (MessageView, Navigator.Extras) -> Unit) {

            if (messageView.who){
                itemView.tv_message_target.visibility = View.VISIBLE
                itemView.tv_message_target.text = messageView.message
                itemView.tv_message.visibility = View.INVISIBLE
                /*val params = itemView.tv_message.layoutParams as ConstraintLayout.LayoutParams
                params.leftToLeft = 40
                itemView.tv_message.requestLayout()
                itemView.tv_message.backgroundResource = (R.drawable.rounded_border)
                itemView.tv_message.textAlignment = View.TEXT_ALIGNMENT_TEXT_END*/
            }else{
                itemView.tv_message.visibility = View.VISIBLE
                itemView.tv_message.text = messageView.message
                itemView.tv_message_target.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener {
                clickListener(messageView,
                        Navigator.Extras(itemView.tv_message))

            }
        }
    }

}