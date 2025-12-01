package com.rija.pmob7

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("en/books")
    fun getBooks(): Call<List<Book>>
}