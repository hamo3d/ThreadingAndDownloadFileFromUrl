package com.example.threading


import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.threading.databinding.ActivityAsyncTaskClassBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AsyncTaskClass : AppCompatActivity() {
    private lateinit var binding: ActivityAsyncTaskClassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAsyncTaskClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e("moh", "ActivityAsyncTaskClassBinding: ${Thread.currentThread().name}")
    }

    override fun onResume() {
        super.onResume()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnStart.setOnClickListener {
            if (checkData()) {
                downloadFile()
            }
        }
    }


    private fun checkData(): Boolean {
        if (binding.editTextText.text.isEmpty()) {
            Toast.makeText(this, "enter url", Toast.LENGTH_LONG).show()

            return false
        }

        return true
    }


    private fun downloadFile() {
        val currentTimestamp = System.currentTimeMillis()
        val url = binding.editTextText.text.toString()
        val fileName = "file_$currentTimestamp.mp3"

        binding.progressBar.progress = 0

        lifecycleScope.launch {
            try {

                // dispatchers.IO : هذه الشائعة عند استخدام تنزيل ملف لتجنب حظر واجهة مستخدم
                val downloadedFile = withContext(Dispatchers.IO) {
                    downloadWithProgress(url, fileName) { progress ->
                        updateProgress(progress)
                    }
                }

                binding.textView3.text = "Downloaded file path: $downloadedFile"
            } catch (e: Exception) {
                binding.textView3.text = "Download failed: ${e.message}"

            }
        }
    }


    private suspend fun downloadWithProgress(
        url: String,
        fileName: String,
        onProgress: (Int) -> Unit
    ): File {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Failed to download: ${response.message}")
        }

        //حجم ملف مراد تنزيله
        val contentLength = response.body?.contentLength() ?: 0
        var downloaded = 0L

        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        directory.mkdirs() // Ensure the "Downloads" directory exists
        val file = File(directory, fileName)
        val outputStream = FileOutputStream(file)
        val responseBody = response.body ?: throw IOException("Response body is null")

        responseBody.byteStream().use { input ->
            val buffer = ByteArray(8192)
            var bytesRead: Int

            while (input.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                downloaded += bytesRead

                val progress = (downloaded * 100 / contentLength).toInt()
                onProgress(progress)
            }
        }

        outputStream.flush()
        outputStream.close()
        binding.progressBar.progress = 0
        return file
    }

    private fun updateProgress(progress: Int) {
        binding.progressBar.progress = progress
    }
}
