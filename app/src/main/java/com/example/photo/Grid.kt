package com.example.photo

import android.os.Bundle
import android.os.RecoverySystem
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Grid : Fragment(), delet, delt {
    lateinit var  adapter: GridImageAdapter
lateinit var  repository: Repository
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
}