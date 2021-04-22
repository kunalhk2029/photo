package com.example.photo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.ByteArrayOutputStream


class TypeConverters {


    @TypeConverter
    fun fromBitmapToByteArray(bitmap: Bitmap) : ByteArray {
        val outputStream = ByteArrayOutputStream()
         bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return  outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmapFromByteArray(byteArray: ByteArray) : Bitmap{
        return  BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
}