package com.globalhiddenodds.tasos.models.persistent.files

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.tools.Variables
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.io.*
import javax.inject.Singleton

@Singleton
class ManageFiles @Inject constructor(private val context: Context) {
    private val directoryWork = "tasos"
    private val quality = 100
    val fileNameML: String
        get() = "dataml.txt"

    fun getAlbumStorageDir(): File {
        val file = File(Environment.getExternalStorageDirectory(), directoryWork)
        if (!file.exists()) {
            file.mkdirs()
            println("Directory not created")
        }
        return file
    }

    fun writeFile(fileName: String, data: String){
        GlobalScope.launch {
            try {
                val file = File(getAlbumStorageDir(), fileName)
                if (!file.exists()){
                    file.createNewFile()
                }
                val out = BufferedWriter(FileWriter(file, true) as Writer?, 1024)
                out.write(data)
                out.newLine()
                out.close()

            }catch (io: IOException){
                println(io.message)
            }
        }
    }

    fun readFromFile(fileName: String): String? {

        try {
            val file = File(getAlbumStorageDir(), fileName)
            val bufferedReader = BufferedReader(FileReader(file))
            val stringBuilder = StringBuilder()
            var lineTxt = bufferedReader.readLine()
            while (!lineTxt.isNullOrEmpty()) {
                //stringBuilder.append(lineTxt)
                stringBuilder.appendln(lineTxt)
                lineTxt = bufferedReader.readLine()
            }

            return stringBuilder.toString()

        } catch (e: FileNotFoundException) {
            println("File not found: " + e.toString())
        } catch (e: IOException) {
            println("Can not read file: " + e.toString())
        }
        return null

    }

    private fun scaleBitmapDown(bitmap: Bitmap): Bitmap {
        if (Variables.screenWidth < bitmap.width){
            val diffRelationSize = Variables.screenWidth.toFloat() / bitmap.width.toFloat()
            val newWidth = (bitmap.width.toFloat() * diffRelationSize).toInt()
            val newHeight = (bitmap.height.toFloat() * diffRelationSize).toInt()
            val newBitmap = Bitmap.createBitmap(newWidth,
                    newHeight, Bitmap.Config.ARGB_8888)

            val ratioX = newWidth / bitmap.width.toFloat()
            val ratioY = newHeight / bitmap.height.toFloat()
            val middleX = newWidth / 2.0f
            val middleY = newHeight / 2.0f

            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

            val c = Canvas(newBitmap)
            c.matrix = scaleMatrix
            c.drawBitmap(bitmap, middleX - bitmap.width/2,
                    middleY - bitmap.height/2,
                    Paint(Paint.FILTER_BITMAP_FLAG))
            return newBitmap

        }else{
            return bitmap
        }
    }

    fun getBitmap(uri: Uri?): Bitmap? {
        when {
            uri != null -> return try {
                scaleBitmapDown(
                        MediaStore.Images.Media
                                .getBitmap(context.contentResolver, uri))

            } catch (e: IOException) {
                println(e.message)
                null
            }
            else -> {
                println(R.string.value_null)
                return null
            }

        }
    }

    fun base641EncodedImage(bitmap: Bitmap): String{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    fun base64DecodeImage(baseString: String): Bitmap{
        val decode: ByteArray = Base64.decode(baseString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decode,
                0, decode.size)
    }



}