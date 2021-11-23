package com.example.philomathy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadActivity extends AppCompatActivity {

    ImageButton upload_button;
    ImageView img_preview;
    StorageReference storageReference;
    Button postbutton;
    DatabaseReference databaseReference;
    private Uri filePath;
    ProgressDialog progressDialog;
    int PICK_IMAGE_REQUEST = 100;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        postbutton = findViewById(R.id.postbutton);
        upload_button = findViewById(R.id.upload_button);
        img_preview = findViewById(R.id.uploaded_view);

        upload_button.setOnClickListener(
                v -> selectImage());

        postbutton.setOnClickListener(v -> uploadImage());


    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,  100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
                data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img_preview.setImageBitmap(bitmap);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filePath != null){ final ProgressDialog progress = new ProgressDialog(this); progress.setTitle("Uploading...."); progress.show();

            StorageReference ref= storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.dismiss();
                    Toast.makeText(UploadActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
//                    String imageUrl = taskSnapshot.getDownloadUrl().toString(); /* Fetch url image */
//                    Picasso.with(getBaseContext()).load(imageUrl).into(imgFirebase);
//                     use picasso to fetch url and display image */
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
                    Toast.makeText(UploadActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progres_time = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progress.setMessage("Uploaded "+(int)progres_time+" %");
                }
            });
        }
    }




}