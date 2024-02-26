package edu.quinnipiac.ser210.assignment2
/**
  * Sam Woodburn
  * 2/25/2024
  * SER210 Assignment 2- Four In A Row
  * main activity
 */

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //connect to xml
        setContentView(R.layout.activity_main)
    }
}