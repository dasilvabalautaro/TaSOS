package com.globalhiddenodds.tasos.models.persistent.database.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.globalhiddenodds.tasos.models.persistent.database.data.MessageData
import com.globalhiddenodds.tasos.models.persistent.database.data.SSetContacts
import com.globalhiddenodds.tasos.models.persistent.database.data.SSetMessages
import com.globalhiddenodds.tasos.models.persistent.database.data.SSetNewMessageQuantity

@Dao
interface MessageDataDao {

    @Query("SELECT * FROM messageData")
    fun getAll(): List<MessageData>

    @Query("SELECT source, SUM(source) AS quantity FROM messageData WHERE state == 1 GROUP BY source ORDER BY dateMessage")
    fun getNewMessage(): List<SSetNewMessageQuantity>

    @Query("SELECT source FROM messageData GROUP BY source ORDER BY dateMessage")
    fun getContacts(): LiveData<List<SSetContacts>>

    @Query("SELECT message, dateMessage FROM messageData WHERE source LIKE :source ORDER BY dateMessage")
    fun getMessageOfContact(source: String): LiveData<List<SSetMessages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messageData: MessageData)

    @Query("DELETE FROM messageData")
    fun deleteAll()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(messageData: MessageData)

}