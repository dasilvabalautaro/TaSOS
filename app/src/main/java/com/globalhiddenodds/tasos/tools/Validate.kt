package com.globalhiddenodds.tasos.tools

object Validate {
    fun isValidEmail(value: CharSequence?): Boolean{
        return if (value == null){
            false
        }else{
            android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
        }
    }

}