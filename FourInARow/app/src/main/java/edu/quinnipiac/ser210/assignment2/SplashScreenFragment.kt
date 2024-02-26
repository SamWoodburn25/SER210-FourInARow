package edu.quinnipiac.ser210.assignment2

/**
  * Sam Woodburn
  * 2/25/2024
  * SER210 Assignment 2- Four In A Row
  * splash screen fragment- welcome screen with space for player name and start button
 */

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation

class SplashScreenFragment : Fragment(){

    //nav controller for navigation to next fragment
    lateinit var navController: NavController
    //shared view model to store the player name
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //find nav controller
        navController = Navigation.findNavController(view)
        //find the edit text box
        val editText = view.findViewById<EditText>(R.id.name)
        //on start button clicked
        view.findViewById<Button>(R.id.start).setOnClickListener{
            //users name
            val text = editText.text.toString()
            //set the name in the shared view model, so it can be accessed by playboard frag
            sharedViewModel.setText(text)
            //navigate to next screen
            navController.navigate(R.id.action_SplashScreenFragment_to_playboardFragment)
        }
    }


}