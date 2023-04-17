package com.example.movieapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val castButton = findViewById<Button>(R.id.castButton)
        castButton.setOnClickListener{
            val goToCastActivity = Intent(this, CastActivity::class.java)
            startActivity(goToCastActivity)
        }

        val purchaseButton = findViewById<Button>(R.id.purchaseButton)
        purchaseButton.setOnClickListener{
            val goToPurchaseActivity = Intent(this, PurchaseActivity::class.java)
            startActivity(goToPurchaseActivity)
        }

    }
}
