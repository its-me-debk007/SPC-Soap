package `in`.silive.spc.retrofit

import `in`.silive.spc.model.data
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query



interface Api {

    @GET("/payment/verifyqr/{token}")
    fun getData(@Path("token") token:String
                ,@Query("view") value:Boolean
    ):Call<data>
    @GET("/payment/verifyqr/{token}")
    fun postData(@Path("token") token:String
    ):Call<ResponseBody>
}