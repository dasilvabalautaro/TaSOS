package com.globalhiddenodds.tasos.presentation.component

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
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
import com.github.chrisbanes.photoview.PhotoView
import com.globalhiddenodds.tasos.tools.Variables
import org.jetbrains.anko.layoutInflater
import kotlin.properties.Delegates


class MessageAdapter @Inject constructor():
        RecyclerView.Adapter<MessageAdapter.ViewHolder>() {


    internal var collection: List<MessageView> by Delegates.observable(emptyList()) {
        _, _, _ -> notifyDataSetChanged()
    }
//    internal val collection: ArrayList<MessageView> = ArrayList()

    internal var clickListener: (MessageView,
                                 Navigator.Extras) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MessageAdapter.ViewHolder(parent.inflate(R.layout.view_row_message))

    override fun onBindViewHolder(viewHolder: MessageAdapter.ViewHolder,
                                  position: Int) =
            viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

   /* fun setObjectList(itemList: ArrayList<MessageView>){
        val diffResult: DiffUtil.DiffResult = DiffUtil
                .calculateDiff(DataListDiffCallback(this
                        .collection, itemList))
        this.collection.clear()
        this.collection.addAll(itemList)
        Thread.sleep(1000)
        diffResult.dispatchUpdatesTo(this)
    }*/

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeImage = 2

        @SuppressLint("SimpleDateFormat")
        fun bind(messageView: MessageView, clickListener: (MessageView,
                                                           Navigator.Extras) -> Unit) {


            val dateString = SimpleDateFormat("MM/dd")
                    .format(Date(messageView.dateMessage))
            itemView.iv_msg_img.setImageBitmap(null)
            itemView.tv_message_target.text = ""
            itemView.tv_date_message_target.text = ""
            itemView.tv_message.text = ""
            itemView.tv_date_message.text = ""
            itemView.tv_message_target.background = null
            itemView.tv_message.background = null

          /* val params = itemView.tv_message.layoutParams as ConstraintLayout.LayoutParams
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
                params.leftToLeft = 60
                itemView.tv_message.requestLayout()
                itemView.tv_message.backgroundResource = (R.drawable.rounded_border)
                itemView.tv_message.textAlignment = View.TEXT_ALIGNMENT_TEXT_END*/



       /*     val builder = SpannableStringBuilder(itemView.tv_message.text)
            val spans = builder.getSpans(0,
                    1, ImageSpan::class.java)
            if (spans.isNotEmpty()){
                for (i in 0 until spans.size){
                    builder.removeSpan(spans[i])
                }
                itemView.tv_message.layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
                itemView.tv_message.layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            }

            val builderT = SpannableStringBuilder(itemView.tv_message_target.text)
            val spansT = builderT.getSpans(0,
                    1, ImageSpan::class.java)
            if (spansT.isNotEmpty()){
                for (i in 0 until spansT.size){
                    builderT.removeSpan(spansT[i])
                }
                itemView.tv_message_target.layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
                itemView.tv_message_target.layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

            }*/

            if (messageView.who){
               /* itemView.tv_message_target.visibility = View.VISIBLE
                itemView.tv_date_message_target.visibility = View.VISIBLE
                itemView.tv_date_message_target.text = dateString
*/
                if(messageView.type == typeImage){
                   /* itemView.tv_message_target.visibility = View.INVISIBLE
                    itemView.tv_date_message_target.visibility = View.INVISIBLE
                    itemView.tv_message.visibility = View.INVISIBLE
                    itemView.tv_date_message.visibility = View.INVISIBLE
*/
                    var bm = base64DecodeImage(messageView.message)
                    bm = verifySize(bm)
                    itemView.iv_msg_img.setImageBitmap(bm)
/*
                    try {
                        itemView.tv_message_target.text = setImage(bm,
                                itemView.tv_message_target)

                    }catch (ex: Exception){
                        println("ERROR TEXTVIEW IMAGE " + ex.message)
                    }
*/


                }else{
                    //itemView.iv_msg_img.visibility = View.INVISIBLE
                    /*itemView.tv_message_target.visibility = View.VISIBLE
                    itemView.tv_date_message_target.visibility = View.VISIBLE*/
                    itemView.tv_date_message_target.text = dateString
                   /* itemView.tv_message.visibility = View.INVISIBLE
                    itemView.tv_date_message.visibility = View.INVISIBLE
*/                  itemView.tv_message_target
                            .setBackgroundResource(R.drawable.bkg_msg_target)
                    itemView.tv_message_target.text = messageView.message

                }

               /* itemView.tv_message.visibility = View.INVISIBLE
                itemView.tv_date_message.visibility = View.INVISIBLE*/

            }else{
               /* itemView.tv_message.visibility = View.VISIBLE
                itemView.tv_date_message.visibility = View .VISIBLE*/

                if(messageView.type == typeImage){
                  /*  itemView.tv_message.visibility = View.INVISIBLE
                    itemView.tv_date_message.visibility = View.INVISIBLE
                    itemView.tv_message_target.visibility = View.INVISIBLE
                    itemView.tv_date_message_target.visibility = View.INVISIBLE
*/

                    var bm = base64DecodeImage(messageView.message)
                    bm = verifySize(bm)
                    itemView.iv_msg_img.setImageBitmap(bm)
                   /* try {
                        itemView.tv_message.text = setImage(bm, itemView.tv_message)
                    }catch (ex: Exception){
                        println("ERROR TEXTVIEW IMAGE " + ex.message)
                    }*/


                }else{
                    //itemView.iv_msg_img.visibility = View.INVISIBLE
                   /* itemView.tv_message.visibility = View.VISIBLE
                    itemView.tv_date_message.visibility = View.VISIBLE*/
                    itemView.tv_date_message.text = dateString
                   /* itemView.tv_message_target.visibility = View.INVISIBLE
                    itemView.tv_date_message_target.visibility = View.INVISIBLE*/
                    itemView.tv_message
                            .setBackgroundResource(R.drawable.bkg_msg_source)
                    itemView.tv_message.text = messageView.message
                }


            }

            itemView.setOnClickListener {
               /* clickListener(messageView,
                        Navigator.Extras(itemView.iv_msg_img))*/

            }

            itemView.iv_msg_img.setOnClickListener {
                itemView.iv_msg_img.isDrawingCacheEnabled = true

                val zoomImage = AlertDialog.Builder(itemView.iv_msg_img.context)
                val imgView = itemView.iv_msg_img.context.layoutInflater
                        .inflate(R.layout.view_zoom_image, null)
                val photoView = imgView.findViewById<PhotoView>(R.id.pv_zoom)
                photoView.setImageBitmap(itemView.iv_msg_img.drawingCache)
                zoomImage.setView(imgView)
                val dialog = zoomImage.create()
                dialog.show()

            }

        }

        private fun verifySize(bitmap: Bitmap): Bitmap{
            return if (Variables.screenWidth < bitmap.width){
                val diffRelationSize = Variables.screenWidth.toFloat() / bitmap.width.toFloat()
                Bitmap.createScaledBitmap(bitmap,
                        (bitmap.width * diffRelationSize).toInt(),
                        (bitmap.height * diffRelationSize).toInt(), true)
            }else{
                bitmap
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