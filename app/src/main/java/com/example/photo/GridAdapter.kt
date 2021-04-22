package com.example.photo

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.loader.ResourcesProvider
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class GridImageAdapter(val context: Context,val listener: delt):RecyclerView.Adapter<GridSavedImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridSavedImageViewHolder {
  val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item,parent,false)
        val holder = GridSavedImageViewHolder(view)
        holder.itemView.setOnClickListener {
            listener.openFullSizePhoto(holder.adapterPosition)
        }
        return  holder
     }

    val diffUtilCallback = object : DiffUtil.ItemCallback<ImageData>() {

        override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
               return oldItem.id==newItem.id
           }
           @SuppressLint("DiffUtilEquals")
           override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
                 return oldItem==newItem
           }
       }
    val diffutil = AsyncListDiffer(this,diffUtilCallback)

    override fun onBindViewHolder(holder: GridSavedImageViewHolder, position: Int) {
         val pos = diffutil.currentList[position]
            holder.apply {
//                datee.text = pos.date
//                val byteArray = pos.bitmapimage
//               val bitmap : Bitmap= BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)\


                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),pos.bitmapimage).absolutePath
                val filee = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),pos.bitmapimage)


//                image.setImageResource(R.drawable.background)

//                val drawablefrombitmap= BitmapDrawable(Resources.getSystem(),
//                    decodeSampledBitmapFromFile(file,135,135))
//                image.setImageDrawable(drawablefrombitmap)
//                image.setImageBitmap(decodeSampledBitmapFromFile(file,540,960))
//                image.setImageResource(R.drawable.tm)
//                  image.load(R.drawable.full)
//                image.setImageResource(R.drawable.full)
            GlobalScope.launch {
                val fgh = BitmapFactory.decodeFile(file)
                val sdh = decodeSampledBitmapFromFile(filee,540,960)
                   withContext(Dispatchers.Main)
                   {
                       Glide.with(context).load(sdh).into(image)

                   }

            }
            }
    }
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
    fun decodeSampledBitmapFromFile(
        res: File,

        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(res.absolutePath, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeFile(res.absolutePath ,this)
        }
    }

    override fun getItemCount(): Int {
     return diffutil.currentList.size
    }

}

class GridSavedImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val image : ImageView = itemView.findViewById(R.id.imagegrid)
}

interface delt
{
    fun openFullSizePhoto(position: Int)
    {

    }
}

