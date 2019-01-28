package com.globalhiddenodds.tasos.tools

object Validate {
    fun isValidEmail(value: CharSequence?): Boolean{
        return if (value == null){
            false
        }else{
            android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
        }
    }

    fun getName(name: String): String{
        val splitPointName = name.split(".")
        var filterName = splitPointName[0]
        val splitDownStripe = filterName.split("_")
        filterName = splitDownStripe[0]
        val splitMiddleStripe = filterName.split("-")
        filterName = splitMiddleStripe[0]
        return filterName
    }

}