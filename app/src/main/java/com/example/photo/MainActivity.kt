package com.example.photo

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val builder = VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
    }

//    val c = findNavController(R.id.search).navigate(R.id.action_search_to_results)
}