package edu.quinnipiac.ser210.assignment2

/**
  * Sam Woodburn
  * 2/25/2024
  * SER210 Assignment 2- Four In A Row
  * playboard fragment- displays 36 image buttons as the game board, which change when clicked
 */

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import org.w3c.dom.Text
import java.util.Timer
import java.util.TimerTask
import java.util.logging.Handler
import kotlin.random.Random
import kotlin.system.exitProcess

class PlayboardFragment : Fragment(), View.OnClickListener{

    //variables
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var player: Int = 0
    private lateinit var player_name: String
    private lateinit var buttonList: List<ImageButton>
    private lateinit var FIR_board: FourInARow
    var currentState: Int = GameConstants.PLAYING


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get users inputted name
        sharedViewModel.text.observe(viewLifecycleOwner){
            s ->
            val textView = view.findViewById<TextView>(R.id.enteredName)
            player_name = s
            //set the welcome statement to include players name
            textView.text = "Welcome $s!"
            //set the current turn text view
            view?.findViewById<TextView>(R.id.player_turn)?.setText("Current Turn: $player_name")
        }
        //make list of image buttons
        val buttons = listOf(
            R.id.button0,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.button10,
            R.id.button11,
            R.id.button12,
            R.id.button13,
            R.id.button14,
            R.id.button15,
            R.id.button16,
            R.id.button17,
            R.id.button18,
            R.id.button19,
            R.id.button20,
            R.id.button21,
            R.id.button22,
            R.id.button23,
            R.id.button24,
            R.id.button25,
            R.id.button26,
            R.id.button27,
            R.id.button28,
            R.id.button29,
            R.id.button30,
            R.id.button31,
            R.id.button32,
            R.id.button33,
            R.id.button34,
            R.id.button35
        )
        //set button click listeners
        buttons.forEach { button ->
            view.findViewById<ImageButton>(button).setOnClickListener(this)
        }

        //make a List of the buttons,  which maps to the image button
        buttonList = buttons.map { id ->
            view.findViewById<ImageButton>(id)
        }
        //make game board array
        FIR_board = FourInARow(buttonList)

        /* reset button */
        view.findViewById<Button>(R.id.reset).setOnClickListener{
            FIR_board.clearBoard()
            currentState = GameConstants.PLAYING
            view.findViewById<TextView>(R.id.winner).setText(R.string.empty)
        }
    }

    override fun onClick(buttonView: View?) {
        //move set variable
        var moveset: Boolean = false
        //first the player clicks a square, so player 0
        player = 2
        //buttonView is the button which was clicked, so we are checking the tag, then coloring the correct button
        val buttonTag = buttonView?.tag.toString()
        when (buttonTag) {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35" -> {
                //now we have which button was clicked by the user, make it an int
                var buttonTagInt = buttonTag.toInt()
                    //check if the space is available
                    if(FIR_board.isSpotAvailable(buttonTagInt)) {
                        //if it is available change the box color
                        (buttonView as ImageButton)?.setImageResource(R.drawable.red)
                        //set the move in the array
                        FIR_board.setMove(player, buttonTagInt)
                        //set error message back to empty
                        view?.findViewById<TextView>(R.id.error)?.setText(R.string.empty)
                        //move set it true
                        moveset = true
                    }
                    else{
                        //if space is not available error message
                        view?.findViewById<TextView>(R.id.error)?.setText(R.string.error)
                        //set moveset to false
                        moveset = false
                    }
                }
        }

        //check for winner within the game board array
        if (FIR_board.checkForWinner() == 2) {
            currentState = GameConstants.RED_WON
            view?.findViewById<TextView>(R.id.winner)?.setText(R.string.red_won)
        }
        if (FIR_board.checkForWinner() == 3) {
            currentState = GameConstants.BLUE_WON
            view?.findViewById<TextView>(R.id.winner)?.setText(R.string.blue_won)
        }
        if (FIR_board.checkForWinner() == 1) {
            currentState = GameConstants.TIE
            view?.findViewById<TextView>(R.id.winner)?.setText(R.string.tied)
        }

        //if the move was set and the player does not need to pick again, play the computer move and check for the winner
        if(moveset && FIR_board.checkForWinner() ==0) {
            //switch players
            player = 1
            view?.findViewById<TextView>(R.id.player_turn)?.setText("Current Turn: AI")
            //timer delay after the person clicks
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    //get and set the computer move
                    setCMove()
                    //set the player name back to user
                    view?.findViewById<TextView>(R.id.player_turn)
                        ?.setText("Current Turn: $player_name")

                    //check for winner within the game board array again
                    if (FIR_board.checkForWinner() == 2) {
                        currentState = GameConstants.RED_WON
                        view?.findViewById<TextView>(R.id.winner)?.setText(R.string.red_won)
                    }
                    if (FIR_board.checkForWinner() == 3) {
                        currentState = GameConstants.BLUE_WON
                        view?.findViewById<TextView>(R.id.winner)?.setText(R.string.blue_won)
                    }
                    if (FIR_board.checkForWinner() == 1) {
                        currentState = GameConstants.TIE
                        view?.findViewById<TextView>(R.id.winner)?.setText(R.string.tied)
                    }

                }
            }, 1000)

        }

    }

    //set the computer move, making the ai a little smarter to check if the space in the row below is empty, if not getting another random space
    fun setCMove(){
        // get computer move
        var moveSet = false
        var cMove = 0
        //loop to check if the spot picked is available
        outerloop@ while(!moveSet){
            //loop through to find the previous spot chosen
            for (col in 0 until GameConstants.COLS) {
                for (row in GameConstants.ROWS-1 downTo 0) {
                    if(FIR_board.get(row, col) == GameConstants.BLUE){
                        //set cmove to the spot underneath it, if it is within bounds
                        if(row+1 < GameConstants.ROWS){
                            cMove = col + (row+1) * GameConstants.COLS
                            //if the spot is available set it and leave the loop
                            //check if the space is available
                            if(FIR_board.isSpotAvailable(cMove)) {
                                //set the move in the array
                                FIR_board.setMove(player, cMove)
                                //leave the loop
                                moveSet = true
                                break@outerloop
                            }
                        }
                    }
                }
            }
            //if the loop was left and the move was not set, then pick a random spot for cmove
            if (!moveSet){
                do{
                    cMove = Random.nextInt(0, 35)
                }while (!FIR_board.isSpotAvailable(cMove))
                //set the move in the array
                FIR_board.setMove(player, cMove)
                //leave the loop
                moveSet = true
            }
        }
        //get the button corresponding to the computer move picked and change to blue
        when(cMove){
            0 -> view?.findViewById<ImageButton>(R.id.button0)?.setImageResource(R.drawable.blue)
            1 -> view?.findViewById<ImageButton>(R.id.button1)?.setImageResource(R.drawable.blue)
            2 -> view?.findViewById<ImageButton>(R.id.button2)?.setImageResource(R.drawable.blue)
            3 -> view?.findViewById<ImageButton>(R.id.button3)?.setImageResource(R.drawable.blue)
            4 -> view?.findViewById<ImageButton>(R.id.button4)?.setImageResource(R.drawable.blue)
            5 -> view?.findViewById<ImageButton>(R.id.button5)?.setImageResource(R.drawable.blue)
            6 -> view?.findViewById<ImageButton>(R.id.button6)?.setImageResource(R.drawable.blue)
            7 -> view?.findViewById<ImageButton>(R.id.button7)?.setImageResource(R.drawable.blue)
            8 -> view?.findViewById<ImageButton>(R.id.button8)?.setImageResource(R.drawable.blue)
            9 -> view?.findViewById<ImageButton>(R.id.button9)?.setImageResource(R.drawable.blue)
            10 -> view?.findViewById<ImageButton>(R.id.button10)?.setImageResource(R.drawable.blue)
            11 -> view?.findViewById<ImageButton>(R.id.button11)?.setImageResource(R.drawable.blue)
            12 -> view?.findViewById<ImageButton>(R.id.button12)?.setImageResource(R.drawable.blue)
            13 -> view?.findViewById<ImageButton>(R.id.button13)?.setImageResource(R.drawable.blue)
            14 -> view?.findViewById<ImageButton>(R.id.button14)?.setImageResource(R.drawable.blue)
            15 -> view?.findViewById<ImageButton>(R.id.button15)?.setImageResource(R.drawable.blue)
            16 -> view?.findViewById<ImageButton>(R.id.button16)?.setImageResource(R.drawable.blue)
            17 -> view?.findViewById<ImageButton>(R.id.button17)?.setImageResource(R.drawable.blue)
            18 -> view?.findViewById<ImageButton>(R.id.button18)?.setImageResource(R.drawable.blue)
            19 -> view?.findViewById<ImageButton>(R.id.button19)?.setImageResource(R.drawable.blue)
            20 -> view?.findViewById<ImageButton>(R.id.button20)?.setImageResource(R.drawable.blue)
            21 -> view?.findViewById<ImageButton>(R.id.button21)?.setImageResource(R.drawable.blue)
            22 -> view?.findViewById<ImageButton>(R.id.button22)?.setImageResource(R.drawable.blue)
            23 -> view?.findViewById<ImageButton>(R.id.button23)?.setImageResource(R.drawable.blue)
            24 -> view?.findViewById<ImageButton>(R.id.button24)?.setImageResource(R.drawable.blue)
            25 -> view?.findViewById<ImageButton>(R.id.button25)?.setImageResource(R.drawable.blue)
            26 -> view?.findViewById<ImageButton>(R.id.button26)?.setImageResource(R.drawable.blue)
            27 -> view?.findViewById<ImageButton>(R.id.button27)?.setImageResource(R.drawable.blue)
            28 -> view?.findViewById<ImageButton>(R.id.button28)?.setImageResource(R.drawable.blue)
            29 -> view?.findViewById<ImageButton>(R.id.button29)?.setImageResource(R.drawable.blue)
            30 -> view?.findViewById<ImageButton>(R.id.button30)?.setImageResource(R.drawable.blue)
            31 -> view?.findViewById<ImageButton>(R.id.button31)?.setImageResource(R.drawable.blue)
            32 -> view?.findViewById<ImageButton>(R.id.button32)?.setImageResource(R.drawable.blue)
            33 -> view?.findViewById<ImageButton>(R.id.button33)?.setImageResource(R.drawable.blue)
            34 -> view?.findViewById<ImageButton>(R.id.button34)?.setImageResource(R.drawable.blue)
            35 -> view?.findViewById<ImageButton>(R.id.button35)?.setImageResource(R.drawable.blue)
        }
    }


}