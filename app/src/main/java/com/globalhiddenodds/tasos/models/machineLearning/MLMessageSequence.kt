package com.globalhiddenodds.tasos.models.machineLearning
import com.globalhiddenodds.tasos.models.persistent.files.ManageFiles
import javax.inject.Inject

class MLMessageSequence @Inject constructor(private val manageFiles:
                                            ManageFiles) {

    companion object {

        init {
            System.loadLibrary("native-normal-equation")

        }
    }


    private var contentData: String? = null
    private var days: IntArray? = null
    private var hours: LongArray? = null
    private var state: IntArray? = null
    external fun normalEquationFromJNI(days: IntArray,
                                       hours: LongArray,
                                       state: IntArray,
                                       path: String): FloatArray


    fun start(){
        val dirc = manageFiles.getAlbumStorageDir()
        val filePath = dirc.path + "/" + "theta.txt"
        println(dirc.path)
        this.contentData = manageFiles.readFromFile(manageFiles.fileNameML)
        if (this.contentData != null){
            loadVectors()
            val l = normalEquationFromJNI(days!!, hours!!, state!!, filePath)
            for (i in 0 until l.size){
                println(l[i])
            }

        }

    }

    private fun loadVectors(){
        val arrayLine = this.contentData!!.split("\n")
        days = IntArray(arrayLine.size - 1)
        hours = LongArray(arrayLine.size - 1)
        state = IntArray(arrayLine.size - 1)

        if (arrayLine.isNotEmpty()){
            var i = 0
            for (line: String in arrayLine){
                if (!line.isEmpty()){
                    val arrayValue = line.split(",")
                    days!![i] = arrayValue[0].toInt()
                    hours!![i] = arrayValue[1].toLong()
                    state!![i] = arrayValue[2].toInt()
                    i += 1
                }
            }

        }
    }


}