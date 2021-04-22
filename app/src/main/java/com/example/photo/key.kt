package com.example.photo

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageData(
                @ColumnInfo(name = "Image")val bitmapimage : String,
                @PrimaryKey(autoGenerate = true) var id : Int
               )
