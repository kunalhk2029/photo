package com.example.photo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.RecoverySystem
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class Grid : Fragment(), delet, delt {
    lateinit var  adapter: GridImageAdapter
    lateinit var  repository: Repository
    lateinit var list: MutableList<Uri>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        repository = Repository(ImageDatabase.getArticleDatabase(requireContext()))
        repository.data.observe(viewLifecycleOwner,{
//            val size = it.size
//            list = mutableListOf()
//            lifecycleScope.launch {
//            for(i in 0 until size)
//            {
//                val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"${it[i].bitmapimage}")
//                val bitmapp = decodeSampledBitmapFromFile(file,1080,1920)
//                list.addAll(listOf(bitmapp))
//                println("uuuuuuuuuu"+bitmapp.byteCount)
//                val uri = FileProvider.getUriForFile(requireContext(), "com.example.photo.fileprovider", file)
//                list.addAll(listOf(uri))
//                println("uuuuuuuuu"+uri)
//                withContext(Dispatchers.Main)
//                {
//                    adapter.diffutil.submitList(list)
//                }
//            }

//               }
           adapter.diffutil.submitList(it)
        })

    }

    fun setup() {
        val rv=requireView().findViewById<RecyclerView>(R.id.gridrv)
        rv.layoutManager = GridLayoutManager(requireContext(),4)
//        rv.layoutManager=LinearLayoutManager(requireContext())
        adapter = GridImageAdapter(requireContext(),this)
        rv.adapter= adapter

    }

    override fun openFullSizePhoto(position: Int) {
        super.openFullSizePhoto(position)
        val action = GridDirections.actionGridToSavedViewer(position)
        findNavController().navigate(action)

    }

     suspend   fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
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


      suspend  fun decodeSampledBitmapFromFile(
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

}