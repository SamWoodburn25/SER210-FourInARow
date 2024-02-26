package edu.quinnipiac.ser210.assignment2

/**
  * Sam Woodburn
  * 2/25/2024
  * SER210 Assignment 2- Four In A Row
  * FIRGame- makes an array of the game board with methods to clear, set moves, and check for the winner
 */

import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat

class FourInARow(val buttons: List<ImageButton>): IGame {
    // game board in 2D array initialized to zeros
    private var board = Array(GameConstants.ROWS) { IntArray(GameConstants.COLS){0} }

    override fun clearBoard() {
       //reset array
        board = Array(GameConstants.ROWS) { IntArray(GameConstants.COLS){0} }

        // Change each button back to grey
        buttons.forEach { button ->
            button.setImageResource(R.drawable.darkgrey)
        }
    }


    override fun isSpotAvailable(location: Int): Boolean {
        val row = (location) / GameConstants.COLS
        val column = (location) % GameConstants.COLS
        return board[row][column] == GameConstants.EMPTY
    }

    override fun setMove(player: Int, location: Int) {
        var curLocation = location
        while (true) {
            // Get row and column index
            val row = curLocation / GameConstants.COLS // Use GameConstants.COLS for the number of columns
            val column = curLocation % GameConstants.COLS // Use GameConstants.COLS for the number of columns

            // If it's empty, that player fills the spot and exits the loop
            if (board[row][column] == GameConstants.EMPTY) {
                board[row][column] = player
                break
            } else {
                // If it is not empty and the player is human, ask to choose another
                if (player == GameConstants.RED) {
                    println("That space is filled, choose another:")
                    curLocation = readLine()?.toIntOrNull() ?: continue // Read new location and validate
                } else {
                    // computer move logic handled in main
                }
            }
        }
        println("location: $location")
        printBoard()
    }


    //variable for computers move
    override val computerMove: Int = 1

    //get function
    fun get(row: Int, col: Int): Int{
        return board[row][col]
    }


    override fun checkForWinner(): Int {
        //check for horizontal winner
        for (row in 0 until GameConstants.ROWS) {
            for (col in 0 until GameConstants.COLS - 3) {
                val cell = board[row][col]
                if (cell != GameConstants.EMPTY) {
                    var win = true
                    for (i in 0 until 4) { // Check the next 3 cells in the row
                        if (board[row][col + i] != cell) {
                            win = false
                            break
                        }
                    }
                    if (win) {
                        return if (cell == GameConstants.RED) 2 else 3
                    }
                }
            }
        }
        //check for vertical winner
        for (row in 0 until GameConstants.ROWS-3) {
            for (col in 0 until GameConstants.COLS) {
                val cell = board[row][col]
                if (cell != GameConstants.EMPTY) {
                    var win = true
                    for (i in 0 until 4) { // Check the next 3 cells in the column
                        if (board[row + i][col] != cell) {
                            win = false
                            break
                        }
                    }
                    if (win) {
                        return if (cell == GameConstants.RED) 2 else 3
                    }
                }
            }
        }
        //check for diagonal winner
        for (row in 0 until GameConstants.ROWS-3) {
            for (col in 0 until GameConstants.COLS - 3) {
                val cell = board[row][col]
                if (cell != GameConstants.EMPTY) {
                    var win = true
                    for (i in 0 until 4) { // Check the next 3 cells diagonally
                        if (board[row + i][col + i] != cell) {
                            win = false
                            break
                        }
                    }
                    if (win) {
                        return if (cell == GameConstants.RED) 2 else 3
                    }
                }
            }
        }
        return 0
    }

    /**
     * print the board to the log console, for checking if it is correctly updating the game board array
     */
    fun printBoard() {
        for (row in 0 until GameConstants.ROWS) {
            for (col in 0 until GameConstants.COLS) {
                printCell(board[row][col]) // print each of the cells
                if (col != GameConstants.COLS - 1) {
                    print("|") // print vertical partition
                }
            }
            println()
            if (row != GameConstants.ROWS - 1) {
                println("-------------------------") // print horizontal partition
            }
        }
        println()
    }

    /**
     * Print a cell with the specified "content"
     * @param content either BLUE, RED or EMPTY
     */
    fun printCell(content: Int) {
        when (content) {
            GameConstants.EMPTY -> print("   ")
            GameConstants.BLUE -> print(" B ")
            GameConstants.RED -> print(" R ")
        }
    }
}