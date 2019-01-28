package com.globalhiddenodds.tasos

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.globalhiddenodds.tasos.tools.Constants
import com.globalhiddenodds.tasos.models.persistent.PreferenceRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class PreferenceRepositoryTest {

    @Test
    fun preferenceGet(){
        val appContext = InstrumentationRegistry.getTargetContext()

        try {
            val prefs = PreferenceRepository.customPrefs(appContext,
                    Constants.preference_tasos)

            Assert.assertTrue(prefs.contains("initialized"))

        }catch (ex: Exception){
            println(ex.message)
        }

    }
}