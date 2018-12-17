package com.globalhiddenodds.tasos.models.persistent.files

import android.os.Environment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.io.*
import javax.inject.Singleton

@Singleton
class ManageFiles @Inject constructor() {
    private val directoryWork = "tasos"
    val fileNameML = "dataml.txt"

    private fun getAlbumStorageDir(): File {
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
                stringBuilder.append(lineTxt)
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


}