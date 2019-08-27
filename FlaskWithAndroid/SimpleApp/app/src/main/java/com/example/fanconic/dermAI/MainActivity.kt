package com.example.fanconic.dermAI

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.util.Base64
import android.widget.Toast
import com.example.fanconic.dermAI.retrofit.APIKindaStuff
import com.example.fanconic.dermAI.R
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val GALLERY = 1
    private val CAMERA = 2

    // Import API for HTTP requests
    @RequiresApi(Build.VERSION_CODES.M)

    // Create Buttons which can make simple HTTP requests and receive them.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Camera Button
        btnCamera.setOnClickListener {
                showPictureDialog()
        }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    image_view!!.setImageBitmap(bitmap)
                    createJsonQuery(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }

        else if (requestCode == CAMERA)
        {
            val bitmap = data!!.extras!!.get("data") as Bitmap
            image_view!!.setImageBitmap(bitmap)
            createJsonQuery(bitmap)

        }

    }

    private fun createJsonQuery(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val byteArray = baos.toByteArray()
        val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT) as String

        // Create JSON object
        val jsonObj = JsonObject()
        jsonObj.addProperty("chat_id", "fanconic")
        jsonObj.addProperty("encoded_image", encodedImage)

        APIKindaStuff
                .service
                .getVectors(jsonObj)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println("---TTTT :: POST Throwable EXCEPTION:: " + t.message)
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val msg = response.body()?.string()
                            println("---TTTT :: POST msg from server :: " + msg)

                            if (msg != null) {
                                val delimiter = '"'
                                val parts = msg.split(delimiter)
                                val username = parts[3]
                                val prediction = parts[7]
                                val probability = parts[11]

                                val message = "Hello $username!\nYour Mole has been classified as $prediction with a probability of $probability."
                                showAlertDialog(message)

                            }
                        }
                    }
                })
    }



    private fun showAlertDialog(stringToShow: String) {
        val builder1 = AlertDialog.Builder(this@MainActivity)
        builder1.setMessage(stringToShow)
        builder1.setCancelable(true)

        val alert11 = builder1.create()
        alert11.show()

    }



}
