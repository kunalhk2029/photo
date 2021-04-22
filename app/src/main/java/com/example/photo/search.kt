package com.example.photo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import okhttp3.internal.readFieldOrNull
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class search : Fragment() {
    lateinit var repository: Repository
 companion object{
     const val CameraCode =100
     const val requestcode=10

 }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_search, container, false)


        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = Repository(ImageDatabase.getArticleDatabase(requireContext()))
        view.findViewById<EditText>(R.id.SearchBar)
            .setOnEditorActionListener { v, actionId, event ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_GO -> {


                        val qr = view.findViewById<EditText>(R.id.SearchBar)
                        val s = qr.text.toString()

                        if (s.isNotEmpty()) {
                            qr.text.clear()

                            val action = searchDirections.actionSearchToResults(s)
                            Navigation.findNavController(view).navigate(action)

                            val view = activity?.currentFocus
                            if (view != null) {
                                val imm =
                                    context?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(view.windowToken, 0)

                            }

                        } else {
                            Toast.makeText(context, "Enter Something", Toast.LENGTH_LONG).show()
                        }
                        true
                    }
                    else -> false
                }
            }


        view.findViewById<ImageView>(R.id.Search_Button).setOnClickListener {

            val qr = view.findViewById<EditText>(R.id.SearchBar)
            val s = qr.text.toString()

            println(s)

            if (s.isNotEmpty()) {
                qr.text.clear()
                val action = searchDirections.actionSearchToResults(s)
                Navigation.findNavController(view).navigate(action)

                val view = activity?.currentFocus
                if (view != null) {
                    val imm =
                        context?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)

                }

            } else {

                val view = activity?.currentFocus
                if (view != null) {
                    val imm =
                        context?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                Toast.makeText(context, "Enter Something", Toast.LENGTH_LONG).show()
            }
        }


        view.findViewById<Button>(R.id.Saved).setOnClickListener {
            findNavController().navigate(R.id.action_search_to_savedViewer)
        }

       view.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.background)
        view.findViewById<ImageButton>(R.id.camera).setOnClickListener {
            camera()
        }
    }

    fun camera()
    {

        if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, requestcode)

            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
                    CameraCode
                )
            }


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode== CameraCode)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED&&grantResults.isNotEmpty())
            {

                val intent =Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, requestcode)

            }
            else
            {
                Toast.makeText(requireContext(),"Please Grant Camera Permission to work",Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode== requestcode)
            {
                val bitmap : Bitmap = data!!.extras!!.get("data") as Bitmap
                val direc = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

                println("rrrrrrrrrrrrrrr"+data.extras!!.get("data"))
                SaveImageFileFromBitmap(bitmap)
                val file = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            }
        }

    }

    fun SaveImageFileFromBitmap(bitmap: Bitmap)
    {
        val direct = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val filee=File.createTempFile("JPEG_${timeStamp}_",".jpg",direct)
        lifecycleScope.launch {
            repository.insert(ImageData(filee.name,0))
        }
        val fos = FileOutputStream(filee)
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos)
         val byteout= bos.toByteArray()
        fos.write(byteout)
        fos.flush()
        fos.close()

//        FileProvider.getUriForFile(requireContext(),)
    }


}