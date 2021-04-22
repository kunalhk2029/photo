package com.example.photo

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class Adapter(val listener: click):RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
  val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item,parent,false)
        val holder = ViewHolder(view)

       view.setOnClickListener {
            listener.show(holder.adapterPosition)
       }
        return  holder
     }

    val diffUtilCallback = object : DiffUtil.ItemCallback<Image_urls>() {

        override fun areItemsTheSame(oldItem: Image_urls, newItem: Image_urls): Boolean {
               return oldItem.full==newItem.full
           }
           override fun areContentsTheSame(oldItem: Image_urls, newItem: Image_urls): Boolean {
                 return oldItem==newItem }
       }
    val diffutil = AsyncListDiffer(this,diffUtilCallback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val pos = diffutil.currentList[position].raw
        holder.image.apply {
            holder.pb.visibility=View.VISIBLE
            Glide.with(this).load(pos).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false

                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pb.visibility=View.INVISIBLE

                    return false
                }
            }).into(holder.image)
        }
    }


    override fun getItemCount(): Int {
     return diffutil.currentList.size
    }
    fun list() : List<Image_urls>
    {
        return  diffutil.currentList
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
val image : ImageView = itemView.findViewById(R.id.image)
    val pb :ProgressBar = itemView.findViewById(R.id.progress)

}

interface click
{
    fun show(position: Int)
    {

    }
}