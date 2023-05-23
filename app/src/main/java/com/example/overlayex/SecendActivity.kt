package com.example.overlayex

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.overlayex.databinding.ActivitySecendBinding
import com.example.overlayex.network.ApiService
import com.example.overlayex.network.RetrofitNetworkProvider
import com.example.overlayex.network.data.AppVersionDto
import com.example.overlayex.network.data.RequestPost
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SecendActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecendBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        subscribeToUninstaill()

        binding.button.setOnClickListener {
            startGETHttpRequest()
        }
        binding.button2.setOnClickListener {
            startPOSTHttpRequest()
        }
        binding.button3.setOnClickListener {
            startGETHttpListRequest()
        }
    }


    private fun subscribeToUninstaill() {

        val packageManagerApps = packageManager
        val sequenceNumber = getSequenceNumber(this)
        "sequenceNumber = $sequenceNumber".toLog()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val changedPackages = packageManagerApps.getChangedPackages(sequenceNumber)

            if (changedPackages != null) {
                // Packages are changed

                // Get the list of changed packages
                // the list includes new, updated and deleted apps
                val changedPackagesNames = changedPackages.packageNames

                var appName: CharSequence

                for (packageName in changedPackagesNames) {
                    try {
                        appName = packageManagerApps.getApplicationLabel(
                            packageManagerApps.getApplicationInfo(
                                packageName, 0,
                            )
                        )

                        // Either a new or an updated app

                            "New Or Updated App: $packageName , appName = ${appName.toString()}".toLog()

                    } catch (e: PackageManager.NameNotFoundException) {
                        // The app is deleted
                        "Deleted App: $packageName".toLog()
                    }
                }
                saveSequenceNumber(this, changedPackages.sequenceNumber)
            } else {
                // packages not changed
            }
        }
    }

    private fun getSequenceNumber(context: Context): Int {
        val sharedPrefFile = context.getSharedPreferences("your_file_name", MODE_PRIVATE)
        return sharedPrefFile.getInt("sequence_number", 0)
    }

    private fun saveSequenceNumber(context: Context, newSequenceNumber: Int) {
        val sharedPrefFile = context.getSharedPreferences("your_file_name", MODE_PRIVATE)
        val editor = sharedPrefFile.edit()
        editor.putInt("sequence_number", newSequenceNumber)
        editor.apply()
    }


    private fun startGETHttpRequest() {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(getHttpLoggingInterceptor())

        val baseUrl = "https://jsonplaceholder.typicode.com/"
        val gson: Gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val retrofitNetworkProvider = RetrofitNetworkProvider(apiService)

        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitNetworkProvider.get<AppVersionDto>(
                AppVersionDto::class.java,
                "todos".plus("/1"), mapOf("token" to "token_example"), hashMapOf()
            )
            response.toString().toLog()
        }
    }

    private fun startGETHttpListRequest() {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(getHttpLoggingInterceptor())

        val baseUrl = "https://jsonplaceholder.typicode.com/"
        val gson: Gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val retrofitNetworkProvider = RetrofitNetworkProvider(apiService)

        CoroutineScope(Dispatchers.IO).launch {
            val type = object : TypeToken<ArrayList<AppVersionDto>>() {}.type
            val response = retrofitNetworkProvider.get<ArrayList<AppVersionDto>>(
                type,
                "todos", mapOf("token" to "token_example"), hashMapOf()
            )
            response.toString().toLog()
        }
    }

    private fun startPOSTHttpRequest() {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(getHttpLoggingInterceptor())

        val baseUrl = "https://jsonplaceholder.typicode.com/"
        val gson: Gson = GsonBuilder()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        val apiService = retrofit.create(ApiService::class.java)
        val retrofitNetworkProvider = RetrofitNetworkProvider(apiService)
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitNetworkProvider.post<RequestPost, RequestPost>(
                RequestPost::class.java,
                "posts", mapOf("token" to "token_example"), hashMapOf(),
                RequestPost("I'm android developer", 1, "My First Post")
            )
            response.toString().toLog()
        }
    }


    private fun getHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}