package com.example.room.ui.theme

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao

interface ContactDao {
    @Upsert
    suspend fun upsertContacts(contact: Contact)

    @Delete
    suspend fun deleteContacts(contact: Contact)

    @Query("SELECT * FROM contact ORDER BY firstName ASC")
    fun getContactsOrderedByFirstName() : Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY lastName ASC")
    fun getContactsOrderedByLastName() : Flow<List<Contact>>


    @Query("SELECT * FROM contact ORDER BY phoneNumber ASC")
    fun getContactsOrderedByPhoneNumber() : Flow<List<Contact>>

}