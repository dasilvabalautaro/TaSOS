package com.globalhiddenodds.tasos.models.persistent.database.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.globalhiddenodds.tasos.models.persistent.database.data.MessageData
import com.globalhiddenodds.tasos.models.persistent.database.data.SSetNewMessageQuantity

@Dao
interface MessageDataDao {

    @Query("SELECT * FROM messageData")
    fun getAll(): LiveData<List<MessageData>>

    @Query("SELECT source, SUM(state) AS quantity FROM messageData GROUP BY source ORDER BY dateMessage")
    fun getLiveDataContacts(): LiveData<List<SSetNewMessageQuantity>>

    @Query("SELECT * FROM (SELECT * FROM messageData WHERE source LIKE :source ORDER BY dateMessage DESC LIMIT 30) sub ORDER BY dateMessage ASC")
    fun getMessageOfContact(source: String): LiveData<List<MessageData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messageData: MessageData)

    @Query("DELETE FROM messageData")
    fun deleteAll()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(messageData: MessageData)

    @Query("UPDATE messageData SET state = 0 WHERE source LIKE :source AND state == 1")
    fun updateStateMessages(source: String)
}