package com.insta.retrofit_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.insta.retrofit_example.api.ApiRequests
import com.insta.retrofit_example.databinding.ActivityMainBinding
import com.insta.retrofit_example.utils.Constants
import com.insta.retrofit_example.utils.Constants.Companion.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getCurrentData()

        binding.layoutGenerateNewFact.setOnClickListener {
            getCurrentData()
        }
    }

    private fun getCurrentData() {
        binding.tvTextView.visibility = View.GONE
        binding.tvTimeStamp.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getCatFacts().awaitResponse()
                if (response.isSuccessful) {

                    val data = response.body()
                    Log.d(Constants.TAG, data.toString())

                    withContext(Dispatchers.Main) {
                        binding.tvTextView.visibility = View.VISIBLE
                        binding.tvTimeStamp.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.tvTimeStamp.text = data?.createdAt
                        binding.tvTextView.text = data?.text
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "Seems like something went wrong...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}