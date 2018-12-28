package com.globalhiddenodds.tasos.presentation.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import com.globalhiddenodds.tasos.App
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.extension.inflate
import com.globalhiddenodds.tasos.presentation.data.MessageView
import com.globalhiddenodds.tasos.presentation.navigation.Navigator
import kotlinx.android.synthetic.main.view_row_message.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.widget.TextView


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
        Thread.sleep(1000)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeImage = 2

        @SuppressLint("SimpleDateFormat")
        fun bind(messageView: MessageView, clickListener: (MessageView, Navigator.Extras) -> Unit) {


            val dateString = SimpleDateFormat("MM/dd")
                    .format(Date(messageView.dateMessage))

            if (messageView.who){
                itemView.tv_message_target.visibility = View.VISIBLE
                itemView.tv_date_message_target.visibility = View.VISIBLE
                itemView.tv_date_message_target.text = dateString
                if(messageView.type == typeImage){
                    val bm = base64DecodeImage(messageView.message)

                    try {
                        itemView.tv_message_target.text = setImage(bm,
                                itemView.tv_message_target)

                    }catch (ex: Exception){
                        println("ERROR TEXTVIEW IMAGE " + ex.message)
                    }


                }else{
                    itemView.tv_message_target.text = messageView.message

                }

                itemView.tv_message.visibility = View.INVISIBLE
                itemView.tv_date_message.visibility = View .INVISIBLE

            }else{
                itemView.tv_message.visibility = View.VISIBLE
                itemView.tv_date_message.visibility = View .VISIBLE
                if(messageView.type == typeImage){
                    val bm = base64DecodeImage(messageView.message)
                    try {
                        itemView.tv_message.text = setImage(bm, itemView.tv_message)
                    }catch (ex: Exception){
                        println("ERROR TEXTVIEW IMAGE " + ex.message)
                    }


                }else{
                    itemView.tv_message.text = messageView.message
                }
                itemView.tv_date_message.text = dateString
                itemView.tv_message_target.visibility = View.INVISIBLE
                itemView.tv_date_message_target.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener {
                clickListener(messageView,
                        Navigator.Extras(itemView.tv_message))

            }
        }

        private fun base64DecodeImage(baseString: String): Bitmap {
            val decode: ByteArray = Base64.decode(baseString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decode,
                    0, decode.size)
        }

        private fun bitmapToDrawable(bitmap:Bitmap):BitmapDrawable{
            return BitmapDrawable(App.appComponent
                    .context().resources,bitmap)
        }

        private fun setImage(img: Bitmap, txt: TextView): SpannableStringBuilder {
            return SpannableStringBuilder("I").apply {
                setSpan(ImageSpan(txt.context, img),
                        0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }


    }

}