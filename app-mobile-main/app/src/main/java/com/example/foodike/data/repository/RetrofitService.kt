package com.example.foodike.data.repository

import com.example.foodike.domain.model.Data
import com.example.foodike.domain.model.RemoteResult
import com.example.foodike.domain.model.ResponseData
import com.example.foodike.domain.model.TodoItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitService{
    @GET("todos")
        suspend fun listaEmpleados(): List<ResponseData>
}

object RetrofitServiceFactory {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    fun makeRetrofitService(): RetrofitService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RetrofitService::class.java)
    }
}
