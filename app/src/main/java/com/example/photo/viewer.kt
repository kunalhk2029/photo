package com.example.photo

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.executeBlocking
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.launch
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class viewer : Fragment() {

    val arg : viewerArgs? by navArgs()
lateinit var repository: Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        repository = Repository(ImageDatabase.getArticleDatabase(requireContext()))

        // Inflate the layout for this fragmen
        val view=inflater.inflate(R.layout.fragment_viewer, container, false)
             val  sd =view.findViewById<ImageView>(R.id.myZoomageView)
        view.findViewById<ProgressBar>(R.id.progress).visibility=View.VISIBLE
         glide(sd)

        view.findViewById<Button>(R.id.download).setOnClickListener{


            lifecycleScope.launch {

                val d :String?= repository.data(arg!!.url.title+".png")?.bitmapimage

                if(d.isNullOrEmpty())
                {
                    insetintoRoomdb(arg!!.url.full,arg!!.url.title)
                    Toast.makeText(activity,"Download Started",Toast.LENGTH_LONG).show()

                }
                else
                {
                    Toast.makeText(activity,"Image Already Exists",Toast.LENGTH_LONG).show()
                }

            }
        }
        return  view
    }

    private suspend fun insetintoRoomdb(url: String,id:String) {


        val imageurl : ImageData = ImageData(id+".png",0)
        repository.insert(imageurl)
        Downloadimage(url,arg!!.url.title)
    }

    private fun Downloadimage(url: String?,id: String) {

        val directory = File(Environment.DIRECTORY_PICTURES)
        if(!directory.exists()) {
            directory.mkdir()
        }
        val uri : Uri = Uri.parse(url)
        val downloadmanager = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(uri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI )
            setAllowedOverRoaming(false)
            setTitle("Demo Download "+url)
            setDestinationInExternalFilesDir(activity,Environment.DIRECTORY_PICTURES,"$id.png")
//            setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,"demoooo.png")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
       downloadmanager.enqueue(request)
    }

    private fun glide(sd : ImageView)
    {
        Glide.with(this).load(arg?.url!!.full).listener(object : RequestListener<Drawable> {
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
                view!!.findViewById<ProgressBar>(R.id.progress).visibility=View.INVISIBLE

                return false
            }
        }).into(sd)
    }

    private suspend fun ImageCoil(url: String) : Bitmap
    {
        val loader  = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext()).
            data(url)
            .build()

        val result  = (loader.execute(request)as SuccessResult).drawable
//        imageView.setImageDrawable(result)
//        imageView.load("https://images.unsplash.com/photo-1585865311579-f3612f824eac?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80")
       return   (result as BitmapDrawable).bitmap
    }
}