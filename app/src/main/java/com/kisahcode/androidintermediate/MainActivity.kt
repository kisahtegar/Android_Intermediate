package com.kisahcode.androidintermediate

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * An activity demonstrating the usage of WebView to display web content.
 *
 * This activity loads a web page from a URL and enables JavaScript. It also shows an alert message
 * when the web page finishes loading and handles JavaScript alert dialogs.
 */
class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the WebView element in the layout
        val webView = findViewById<WebView>(R.id.webView)

        // Enable JavaScript execution in the WebView
        webView.settings.javaScriptEnabled = true

        // Set a WebViewClient to handle page loading events
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // Execute JavaScript to display an alert when the page finishes loading
                view.loadUrl("javascript:alert('Web kisahcode berhasil dimuat')")
            }
        }

        // Set a WebChromeClient to handle JavaScript alert dialogs
        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                // Display a toast message with the alert message
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                // Confirm the result of the JavaScript alert dialog
                result.confirm()
                return true
            }
        }

        // Load the web page from the specified URL
        webView.loadUrl("https://kisahcode.web.app/")
    }
}