package com.example.photo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.photo.Constant.PER_PAGE

class results : Fragment(), click {



    lateinit var adapter: Adapter
    var Page =1
    var Total =0
    var Title =""

    var los : MutableLiveData<MutableList<Image_urls>> = MutableLiveData(mutableListOf())
    val arg : resultsArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(los.value!!.isEmpty()){
            imageloader(arg.q,Page)
            println("IMageLoadrCaalled")
        }

        setup()

    }

    val listener = object : ViewPager2.OnPageChangeCallback()
    {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)

        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
        }
    }

    val OnScrollListener = object: RecyclerView.OnScrollListener(){

        var isscrolling =false

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        isscrolling=true
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutmanager = recyclerView.layoutManager as LinearLayoutManager
            val itemvisible = layoutmanager.childCount
            val firstvisibleitemposition = layoutmanager.findFirstCompletelyVisibleItemPosition()
            val items = PER_PAGE*Page
            val totalItemOnweb = items<=Total

           val lastitemreached = itemvisible+firstvisibleitemposition>=items
            val shouldload= lastitemreached&&isscrolling&&totalItemOnweb

            if(shouldload)
            {
                isscrolling=false
                Page++
                imageloader(arg.q,Page)
            }


        }

    }

    fun imageloader( q :String,page:Int)
    {
        showpb()
        var arr : ArrayList<Image_urls> = ArrayList()
        val url="https://api.unsplash.com/search/photos/?client_id=" +
                "tX2DBGhiJym5iYHSg7n0xu6gSZeMRUDRzFKYzfY7BJ4" + "&query=" + q+
                "&per_page=30&page=" + page
        val imageurl = JsonObjectRequest(
            Request.Method.GET, url, null, Response.Listener {
                val array = it.getJSONArray("results")
                 Total = it.getString("total").toInt()


                for (i in 0  until array.length())

                {
                    val ob = array.getJSONObject(i).getJSONObject("urls").getString("regular")
                    val id = array.getJSONObject(i).getString("id")

                    val oob = array.getJSONObject(i).getJSONObject("urls").getString("full")
                    val df= Image_urls(ob,oob,Total.toInt(),id)
                    arr.add(df)
                    var list = los
                    list?.value.apply {
                        this?.add(df)
                        los.postValue(this)
                    }

//                    println(los?.value.toString())
                    println(i)
                    if(i==array.length()-1){
                    hidepb()
                    }
                }


                if(los.value!!.isNotEmpty()){
                    println("Length"+los.value!!.size)

                    println(los.value!!.toString())
                }

            }, Response.ErrorListener
            {
                Log.d("Error", "Error Ocurred")
            })

        val instance = MySingleton.getInstance(requireContext())
        instance.addToRequestQueue(imageurl)
    }

    override fun show(position: Int) {
        super.show(position)
//        val bundel = Bundle().apply {
//            putString("url",ImagePass(adapter.diffutil.currentList[position].full,adapter.diffutil.currentList[position].id.toString()))
//        }
        val action = resultsDirections.actionResultsToViewer(ImagePass(adapter.diffutil.currentList[position].full,adapter.diffutil.currentList[position].id.toString()))
        findNavController().navigate(action)
    }
    fun setup()
    {
        val rv = view?.findViewById<RecyclerView>(R.id.rv)
        rv?.layoutManager = LinearLayoutManager(activity)

        los.observe(viewLifecycleOwner, Observer {
            adapter.diffutil.submitList(it?.toList())
        })
        adapter= Adapter(this)
        rv?.adapter=adapter
//        rv.addOnLayoutChangeListener()
        rv?.addOnScrollListener(this@results.OnScrollListener)
        println("SEtUpCalled")
    }

    fun showpb() {
        requireView().findViewById<ProgressBar>(R.id.progress_bar)!!.visibility=View.VISIBLE
    }

    fun hidepb() {
        requireView().findViewById<ProgressBar>(R.id.progress_bar)!!.visibility=View.INVISIBLE
    }


}
