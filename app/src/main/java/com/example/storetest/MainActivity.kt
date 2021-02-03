package com.example.storetest

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.storetest.databinding.ActivityMainBinding
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val READ_REQUEST_CODE: Int = 42
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ボタンが押されたらギャラリーを開く
        binding.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, READ_REQUEST_CODE)
        }

    }

    //写真が選択された後の動き
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {
                try {
                    resultData?.data?.also { uri ->
                        val inputStream = contentResolver?.openInputStream(uri)
                        val image = BitmapFactory.decodeStream(inputStream)
                        saveImage(image)
                        val readImage = readImage()
                        binding.imageView.setImageBitmap(readImage)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
     private fun saveImage(bitmap:Bitmap){
         val byteArrOutputStream = ByteArrayOutputStream()
         val outStream = openFileOutput("image.jpg", Context.MODE_PRIVATE)
         bitmap.compress(Bitmap.CompressFormat.JPEG,100,outStream)
         outStream.write(byteArrOutputStream.toByteArray())
         outStream.close()
    }

    private fun readImage():Bitmap{
        val bufferedInputStream = BufferedInputStream(openFileInput("image.jpg"))
        return BitmapFactory.decodeStream(bufferedInputStream)
    }
}