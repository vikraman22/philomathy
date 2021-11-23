package com.example.philomathy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


private const val TAG = "PostsActivity"
class PostsActivity : AppCompatActivity() {

    private lateinit var firestoreDb : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        firestoreDb = FirebaseFirestore.getInstance()
        val postReference = firestoreDb.collection("Uploads")
        postReference.addSnapshotListener{ snapshot, exception ->
            if(exception != null || snapshot ==null)
            {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }

            for(document in snapshot.documents)
                Log.i(TAG, "Document ${document.id}: ${document.data}")
        }

    }
}




