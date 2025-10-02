package org.example.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView

/**
 * PUBLIC_INTERFACE
 * MainActivity is the entry point that renders a 3x3 Tic Tac Toe grid.
 * It alternates turns between two players:
 * - Knight (replacing X)
 * - Queen (replacing O)
 *
 * Tapping an empty cell places the current player's icon.
 * The status text shows the current turn or the result (win/draw).
 */
class MainActivity : Activity() {

    private lateinit var statusText: TextView
    private lateinit var grid: GridLayout
    private lateinit var resetButton: Button

    // 0 = empty, 1 = knight (X), 2 = queen (O)
    private val board = Array(3) { IntArray(3) { 0 } }
    private var knightTurn = true
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        grid = findViewById(R.id.boardGrid)
        resetButton = findViewById(R.id.resetButton)

        // Initialize cells
        val cellIds = arrayOf(
            intArrayOf(R.id.cell_0_0, R.id.cell_0_1, R.id.cell_0_2),
            intArrayOf(R.id.cell_1_0, R.id.cell_1_1, R.id.cell_1_2),
            intArrayOf(R.id.cell_2_0, R.id.cell_2_1, R.id.cell_2_2)
        )

        for (r in 0 until 3) {
            for (c in 0 until 3) {
                val btn: ImageButton = findViewById(cellIds[r][c])
                btn.setImageDrawable(null)
                btn.contentDescription = "Empty"
                btn.setOnClickListener { onCellClicked(r, c, btn) }
            }
        }

        updateStatus()

        resetButton.setOnClickListener {
            resetGame()
        }
    }

    // PUBLIC_INTERFACE
    /**
     * Handles a user tap on a cell: places the current player's icon if valid,
     * checks for a win/draw, and toggles the turn.
     */
    private fun onCellClicked(r: Int, c: Int, view: ImageButton) {
        if (gameOver) return
        if (board[r][c] != 0) return

        if (knightTurn) {
            board[r][c] = 1
            view.setImageResource(R.drawable.ic_knight)
            view.contentDescription = "Knight"
        } else {
            board[r][c] = 2
            view.setImageResource(R.drawable.ic_queen)
            view.contentDescription = "Queen"
        }

        val winner = checkWinner()
        if (winner != 0) {
            gameOver = true
            statusText.text = if (winner == 1) "Knight wins!" else "Queen wins!"
            return
        }

        if (isBoardFull()) {
            gameOver = true
            statusText.text = "It's a draw!"
            return
        }

        knightTurn = !knightTurn
        updateStatus()
    }

    // PUBLIC_INTERFACE
    /**
     * Resets the game state and clears all icons from the grid.
     */
    private fun resetGame() {
        for (r in 0 until 3) {
            for (c in 0 until 3) {
                board[r][c] = 0
            }
        }
        knightTurn = true
        gameOver = false

        // Clear UI
        for (i in 0 until grid.childCount) {
            val v: View = grid.getChildAt(i)
            if (v is ImageButton) {
                v.setImageDrawable(null)
                v.contentDescription = "Empty"
            }
        }
        updateStatus()
    }

    // PUBLIC_INTERFACE
    /**
     * Updates the status text to indicate whose turn it is.
     */
    private fun updateStatus() {
        statusText.text = if (knightTurn) "Player Knight's turn" else "Player Queen's turn"
    }

    // PUBLIC_INTERFACE
    /**
     * Checks the board for a winning line. Returns:
     * 0 = no winner, 1 = knight, 2 = queen.
     */
    private fun checkWinner(): Int {
        val lines = mutableListOf<IntArray>()

        // Rows and columns
        for (i in 0 until 3) {
            lines.add(intArrayOf(board[i][0], board[i][1], board[i][2]))
            lines.add(intArrayOf(board[0][i], board[1][i], board[2][i]))
        }
        // Diagonals
        lines.add(intArrayOf(board[0][0], board[1][1], board[2][2]))
        lines.add(intArrayOf(board[0][2], board[1][1], board[2][0]))

        for (line in lines) {
            if (line[0] != 0 && line[0] == line[1] && line[1] == line[2]) {
                return line[0]
            }
        }
        return 0
    }

    // PUBLIC_INTERFACE
    /**
     * Returns true if all cells are filled.
     */
    private fun isBoardFull(): Boolean {
        for (r in 0 until 3) {
            for (c in 0 until 3) {
                if (board[r][c] == 0) return false
            }
        }
        return true
    }
}
