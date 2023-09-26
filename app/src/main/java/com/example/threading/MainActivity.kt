package com.example.threading

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.threading.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("safs", Thread.currentThread().name)
        Log.e("safs", Thread.currentThread().id.toString())


        binding.button.setOnClickListener {

           Thread {

                for (i in 0 ..6){
                    Log.e("safs", "onCreate: $i", )
                }

               runOnUiThread {
                   binding.textView.text  = "loop fini"
               }
            }.start()




        }
    }
}