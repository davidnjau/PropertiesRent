package com.dave.properties.network

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.dave.properties.MainActivity
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class RetrofitCalls {


    fun registerUser(context: Context, properties: Properties){

        CoroutineScope(Dispatchers.Main).launch {

            val job = Job()
            CoroutineScope(Dispatchers.IO + job).launch {

                addPropertyData(context, properties)
            }.join()
        }

    }
    private suspend fun addPropertyData(
        context: Context,
        properties: Properties,
    ) {

        val job1 = Job()
        CoroutineScope(Dispatchers.Main + job1).launch {

            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Please wait..")
            progressDialog.setMessage("Registration in progress..")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            var messageToast = ""
            val job = Job()


            CoroutineScope(Dispatchers.IO + job).launch {

                val baseUrl = context.getString(UrlData.BASE_URL.message)
                val apiService = RetrofitBuilder.getRetrofit(baseUrl).create(Interface::class.java)

                val apiInterface = apiService.addProperty(properties)
                apiInterface.enqueue(object : Callback<Any> {
                    override fun onResponse(
                        call: Call<Any>,
                        response: Response<Any>
                    ) {

                        CoroutineScope(Dispatchers.Main).launch { progressDialog.dismiss() }

                        if (response.isSuccessful) {

                            messageToast = "Property added successfully."

                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)

                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(context, messageToast, Toast.LENGTH_SHORT).show()
                            }


                        } else {

                            val code = response.code()

                            val objectError = JSONObject(response.errorBody()!!.string())
                            messageToast = objectError.toString()

                            CoroutineScope(Dispatchers.IO).launch {

                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(context, messageToast, Toast.LENGTH_SHORT).show()
                                }


                            }


                        }


                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.e("-*-*error ", t.localizedMessage)
                        messageToast = "There is something wrong. Please try again"
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, messageToast, Toast.LENGTH_SHORT).show()
                        }

                        progressDialog.dismiss()
                    }
                })

            }.join()


        }

    }

//    private fun getProfileDetails(context: Context, accessToken: String, rolesList: List<String>?) {
//
//        var stringStringMap = HashMap<String, String>()
//        stringStringMap["Authorization"] = " Bearer $accessToken"
//
//        val sharedPreferenceStorage = SharedPreferenceStorage(
//            context,
//            context.resources.getString(R.string.app_name)
//        )
//
//        val baseUrl = context.getString(UrlData.BASE_URL.message)
//        val apiService = RetrofitBuilder.getRetrofit(baseUrl).create(Interface::class.java)
//        val apiInterface = apiService.gerProfileDetails(stringStringMap)
//        apiInterface.enqueue(object : Callback<SuccessProfileDetails> {
//            override fun onResponse(
//                call: Call<SuccessProfileDetails>,
//                response: Response<SuccessProfileDetails>
//            ) {
//
//                if (response.isSuccessful) {
//
//                    val responseData = response.body()?.user_details
//                    if (responseData != null) {
//
//                        val id = responseData.id
//                        val name = responseData.name
//                        val email = responseData.email
//                        val phone_number = responseData.phone_number
//                        val avatar = responseData.avatar
//
//                        val hashMap1 = HashMap<String, String>()
//                        hashMap1["email"] = email
//                        hashMap1["id"] = id
//                        hashMap1["name"] = name
//                        hashMap1["phone_number"] = phone_number
//
//                        if (rolesList != null) {
//                            if (rolesList.contains("WAITER") || rolesList.contains("ADMIN")) {
//                                hashMap1["role"] = "WAITER"
//                            } else {
//                                hashMap1["role"] = "USER"
//                            }
//                        }
//
//                        hashMap1["avatar"] = avatar.toString()
//                        sharedPreferenceStorage.saveData(hashMap1, "profileDetails")
//
//
//                    }
//
//
//                } else {
//                    val code = response.code()
//
//                }
//
//
//            }
//
//            override fun onFailure(call: Call<SuccessProfileDetails>, t: Throwable) {
//                Log.e("-*-*error ", t.localizedMessage)
//
//
//            }
//        })
//
//    }


}

