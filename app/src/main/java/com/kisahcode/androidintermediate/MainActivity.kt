package com.kisahcode.androidintermediate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * The main activity of the application.
 *
 * This activity contains a custom EditText and Button. It checks whether the EditText is empty
 * to enable or disable the Button. It also displays a Toast message when the Button is clicked.
 */
class MainActivity : AppCompatActivity() {

    // Declare MyButton and MyEditText variables
    private lateinit var myButton: MyButton
    private lateinit var myEditText: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize MyButton and MyEditText from layout
        myButton = findViewById(R.id.my_button)
        myEditText = findViewById(R.id.my_edit_text)

        // Perform initial check to enable/disable MyButton
        setMyButtonEnable()

        // Add text change listener to MyEditText to enable/disable MyButton
        myEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Update MyButton's enabled state based on text changes
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })

        // Set click listener for MyButton to display a Toast message
        myButton.setOnClickListener { Toast.makeText(this@MainActivity, myEditText.text, Toast.LENGTH_SHORT).show() }
    }

    /**
     * Enables or disables MyButton based on the content of MyEditText.
     */
    private fun setMyButtonEnable() {
        // Get the text from MyEditText
        val result = myEditText.text
        // Enable MyButton if MyEditText contains non-empty text, otherwise disable it
        myButton.isEnabled = result != null && result.toString().isNotEmpty()
    }
}