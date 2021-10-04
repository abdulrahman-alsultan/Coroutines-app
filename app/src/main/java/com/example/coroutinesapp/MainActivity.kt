package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var tvAdvice: TextView
    lateinit var button: Button
    private val url = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAdvice = findViewById(R.id.tvAdvice)
        button = findViewById(R.id.btnGetAdvice)

        button.setOnClickListener {
            setup()
        }

    }

    private fun setup(){
        CoroutineScope(IO).launch {
            val data = async {
                var response=""
                try {
                    response =URL(url).readText(Charsets.UTF_8)
                }catch (e:Exception) {
                    println("Error $e")
                }
                response
            }.await()

            if(data.isNotEmpty()){
                readAdvice(data)
            }
        }
    }

    private suspend fun readAdvice(data: String){
        withContext(Main)
        {
            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val advice = slip.getString("advice")
            tvAdvice.text = advice
        }
    }
}