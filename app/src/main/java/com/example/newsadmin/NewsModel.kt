package com.example.newsadmin

class NewsModel {

    lateinit var title : String
    lateinit var description : String
    lateinit var key : String
    lateinit var img : String

    constructor(title: String, description: String,key : String, img: String) {
        this.title = title
        this.description = description
        this.key = key
        this.img = img
    }
    constructor(){

    }

}