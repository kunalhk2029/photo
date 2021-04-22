package com.example.photo

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {

   @Insert(onConflict =OnConflictStrategy.REPLACE)
    suspend fun insert(key :ImageData)



    @Update
    fun update (key: ImageData)

    @Delete
    suspend fun delete(data: ImageData)

    @Query("SELECT * FROM ImageData ORDER by id ASC ")
    fun getAllData() : LiveData<List<ImageData>>

   @Query("SELECT * FROM ImageData WHERE Image=:t LIMIT 1")
   suspend fun getData(t : String) : ImageData?

}