package com.example.photo

import java.io.Serializable


data class Image_urls(val raw:String, val full:String, val total:Int,val id:String) : Serializable


data class ImagePass(val full:String, val title: String) : Serializable
