package com.insta.retrofit_example

import com.insta.retrofit_example.api.RandomCatFacts
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {

    @GET("/facts/random")
    fun getCatFacts(): Call<RandomCatFacts>
}