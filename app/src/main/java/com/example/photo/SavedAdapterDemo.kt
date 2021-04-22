package com.example.photo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

class SavedImageAdapterDemo(val context: Context):RecyclerView.Adapter<SavedImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
  val view = LayoutInflater.from(parent.context).inflate(R.layout.photosaved_item,parent,false)
        val holder = SavedImageViewHolder(view)

        return  holder
     }

    val diffUtilCallback = object : DiffUtil.ItemCallback<Int>() {

        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
               return oldItem==newItem
           }
           @SuppressLint("DiffUtilEquals")
           override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                 return oldItem==newItem
           }
       }
    val diffutil = AsyncListDiffer(this,diffUtilCallback)

    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
         val pos = diffutil.currentList[position]
            holder.apply {
//                datee.text = pos.date
//                val byteArray = pos.bitmapimage
//               val bitmap : Bitmap= BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
//                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),pos.bitmapimage)
//                image.setImageResource(pos)
        image.load(pos)
            }
    }


    override fun getItemCount(): Int {
     return diffutil.currentList.size
    }
//    fun list() : List<ImageData>
//    {
//        return  diffutil.currentList
//    }
}

class SavedImageViewHolderDemo(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val image : ImageView = itemView.findViewById(R.id.image)
    val pb :ProgressBar = itemView.findViewById(R.id.progress)
    val datee : TextView = itemView.findViewById(R.id.datesaved)

}

