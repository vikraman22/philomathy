package com.example.philomathy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.Toast
import com.example.philomathy.databinding.ActivityUpload2Binding
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class UploadActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivityUpload2Binding
    private lateinit var ImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpload2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.selectimageBtn.setOnClickListener{
            selectImage()
        }

        binding.uploadimageBtn.setOnClickListener{
            uploadImage()
        }
    }

    private fun selectImage(){
        val intent = Intent()
        intent.type = "images/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }

    private fun uploadImage(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(ImageUri).
        addOnSuccessListener{
            binding.firebaseImage.setImageURI(null)
            Toast.makeText(this@UploadActivity2,"Successfully Uploaded",Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing)progressDialog.dismiss()
        }.addOnFailureListener{
            if (progressDialog.isShowing)progressDialog.dismiss()
            Toast.makeText(this@UploadActivity2,"Failed to upload",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?){
        super.onActivityResult(requestCode,resultCode,imageReturnedIntent)

        if(requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = imageReturnedIntent?.data!!
            binding.firebaseImage.setImageURI(ImageUri);
        }
    }
}