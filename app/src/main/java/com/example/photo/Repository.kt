package com.example.photo

import androidx.lifecycle.LiveData
import androidx.room.Database
import com.android.volley.toolbox.ImageRequest

class Repository(database: ImageDatabase) {

    val dao = database.getdao()
    val data : LiveData<List<ImageData>> = dao.getAllData()


    suspend fun  insert(data: ImageData)
    {
        dao.insert(data)
    }



    suspend fun  data(t:String) : ImageData?
    {
        return  dao.getData(t)
    }

    suspend fun  delete(data: ImageData)
    {
        dao.delete(data)
    }

}