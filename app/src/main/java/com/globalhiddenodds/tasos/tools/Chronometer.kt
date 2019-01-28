package com.globalhiddenodds.tasos.tools

import com.globalhiddenodds.tasos.models.persistent.files.ManageFiles
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Chronometer @Inject constructor(private val managerFiles:
                                      ManageFiles){

    fun reset(){
        Variables.timeElapsed = 0L
    }

    fun start(){
        Variables.timeElapsed = System.currentTimeMillis()
    }

    fun stop(): Long{
        return System.currentTimeMillis() - Variables.timeElapsed
    }

    private fun numericalDay(): Int{
        val now = Date()
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun resetDataML(state: Int){
        val elapse = stop()
        val day = numericalDay()
        val data = day.toString() + "," + Variables.timeElapsed.toString() + "," +
                 state.toString()
        managerFiles.writeFile(managerFiles.fileNameML, data)
        reset()
        start()
        println(data)

    }
}