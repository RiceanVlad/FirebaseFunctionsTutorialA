package com.example.firebasefunctionstutoriala

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseMessaging = FirebaseMessaging.getInstance()
        firebaseMessaging.subscribeToTopic("new_user_forums")
            .addOnCompleteListener {
                Log.d("TAG","vlad: ${it.isSuccessful}")
            }


    }
}