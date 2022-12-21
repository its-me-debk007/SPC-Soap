package `in`.silive.spc.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {


    private val client = OkHttpClient.Builder().build()

    fun getInstance(): Retrofit {
        return  Retrofit.Builder()
            .baseUrl("http://44.202.84.23:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun init(): Api
    {
        return getInstance().create(Api::class.java)
    }
}