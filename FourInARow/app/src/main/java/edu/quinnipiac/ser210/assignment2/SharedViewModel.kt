package edu.quinnipiac.ser210.assignment2

/**
  * Sam Woodburn
  * 2/25/2024
  * SER210 Assignment 2- Four In A Row
  * shared view model, extends ViewModel- holds text data so that you can
  * access it and change it in different fragment
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    //variables to store the text data
    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    //function to change the text
    fun setText(input: String){
        _text.value  = input
    }
}