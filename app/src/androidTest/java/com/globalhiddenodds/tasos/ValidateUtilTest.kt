package com.globalhiddenodds.tasos

import android.support.test.runner.AndroidJUnit4
import com.globalhiddenodds.tasos.tools.Validate
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValidateUtilTest {
    @Test
    fun validateEmail(){
        val result = Validate.isValidEmail("david.arturo.silva@gmail.com")
        val expected = true
        Assert.assertEquals("Email correct", expected, result)
    }

    @Test
    fun invalidEmail(){
        val result = Validate.isValidEmail("david.arturo.silvagmail.com")
        val expected = false
        Assert.assertEquals("Email not correct", expected, result)

    }

    @Test
    fun validaName(){
        val result = Validate.getName("david")
        val expected = "david"
        Assert.assertEquals("Name correct", expected, result)
    }

}