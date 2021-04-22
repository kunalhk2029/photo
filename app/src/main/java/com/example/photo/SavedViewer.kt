package com.example.photo

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.RecoverySystem
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.view.doOnLayout
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SavedViewer : Fragment(), delet {

    lateinit var adapter: SavedImageAdapter
    lateinit var adapterdemp: SavedImageAdapterDemo
    lateinit var repository: Repository
    var posinviewpager=0
    val args : SavedViewerArgs? by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view = inflater.inflate(R.layout.fragment_saved_viewer, container, false)
       return  view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
//        repository.data.observe(viewLifecycleOwner,{
//            adapter.diffutil.submitList(it)
//        })
    view.findViewById<ImageButton>(R.id.ShareButton).setOnClickListener {

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),adapter.diffutil.currentList[posinviewpager].bitmapimage).absoluteFile
        val uri = getUriForFile(requireContext(),"com.example.photo.fileprovider",file)
        intent.putExtra(Intent.EXTRA_STREAM,uri)
        intent.type="image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        val chooser = Intent.createChooser(intent,"Hello there see this amazing image downloaded from Photo App")
        startActivity(chooser)
      }
        view.findViewById<ImageButton>(R.id.DeleteButton).setOnClickListener {
            val file  = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),adapter.diffutil.currentList[posinviewpager].bitmapimage)
            file.delete()
            lifecycleScope.launch{
                repository.delete(adapter.diffutil.currentList[posinviewpager])
            }
        }
    }

    private fun setup() {
        repository = Repository(ImageDatabase.getArticleDatabase(requireContext()))

        val rv = requireView().findViewById<ViewPager2>(R.id.savedrv)
        adapter = SavedImageAdapter(requireContext(),this)
        adapterdemp = SavedImageAdapterDemo(requireContext())
        val kk = object : ViewPager2.OnPageChangeCallback()
        {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
//                posinviewpager=position
//                println("Pppppppppppppppppppppppppp"+position )
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                posinviewpager=position
                println("Pppppppppppppppppppppppppp"+position )

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        }
        rv.registerOnPageChangeCallback(kk)
        repository.data.observe(viewLifecycleOwner,{

            if(it.isEmpty()) {
                println("Savedddddddddddddddddddddddddddddddddddddddddd     Empty")
            }

            else {
                println("Savedddddddddddddddddddddddddddddddddddddddddd     Not Empty      ${it}")
            }
            adapter.diffutil.submitList(it)
        })
//        repository.data.value?.apply{
//            adapter.diffutil.submitList(this)
//            println("]]]]]]]]]]]]]"+this)
//        }

        lifecycleScope.launch {

            withContext(Dispatchers.Main)
            {
                delay(200)
                rv.setCurrentItem(args!!.position,false)

            }
        }


        rv.adapter=adapter
        println("[[[[[[[[[[[["+adapter.diffutil.currentList)

    }

}