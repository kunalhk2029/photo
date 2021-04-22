package com.example.photo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = arrayOf(ImageData::class),version = 1,exportSchema = false)
@TypeConverters(com.example.photo.TypeConverters::class)
abstract class ImageDatabase : RoomDatabase(){
    abstract  fun getdao() : Dao


    companion object
    {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getArticleDatabase(context: Context): ImageDatabase{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,

                   ImageDatabase::class.java,
                    "imageurldatabase"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}