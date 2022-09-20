package com.example.webviewing

import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnWebViewActivity : Button = findViewById(R.id.btnWebView)

        btnWebViewActivity.setOnClickListener {
//            val intent = Intent(applicationContext, WebViewActivity::class.java)
//            startActivity(intent)
            val url = "https://www.yahoo.co.jp/"
            val builder : CustomTabsIntent.Builder = CustomTabsIntent.Builder()
            val customTabsIntent: CustomTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(url))
        }
        val btnWebViewActivity2 : Button = findViewById(R.id.btnWebView2)

        btnWebViewActivity2.setOnClickListener {
            val intent = Intent(applicationContext, WebViewActivity::class.java)
            startActivity(intent)
        }

    }
}