import game.GameBoard
import qlearning.Algorithm

import scala.io.StdIn

/**
 * The entry point into the application.
 *
 * @author mnorthern
 */
object Main {
    def main(args: Array[String]): Unit = {
        val game: GameBoard = new GameBoard
        val algorithm: Algorithm = new Algorithm

        var width = 0
        var height = 0
        var numMines = 0
        var useBigState = false

        println("Welcome to the Minesweeper Solver! (Project Codename: Whispers)\n" +
            "This program will train an AI to play Minesweeper using machine learning.\n" +
            "This program is very intensive and can take thousands of iterations to learn, so please be patient.\n" +
            "Please follow the prompts to set up a custom algorithm, or hit enter 3 times for the default.\n")

        // Accept input for board size
        try {
            println("Please enter the desired width of the board: ")
            width = StdIn.readInt()
            println("Please enter the desired height of the board: ")
            height = StdIn.readInt()
            println("Please enter the desired number of mines: ")
            numMines = StdIn.readInt()
            println("Please enter 0 if you want to use small states (3x3, recommended for most computers) or any other integer if you want to use big states (5x5).\n" +
                "Using small state will run the program much faster and use much less memory, but will be less accurate after training.")
            useBigState = if (StdIn.readInt() == 0) false else true
        } catch {
            case e: Exception => {
                println("Error reading input, using default values for the board (9x9 with 10 mines and small state (3x3))")
                width = 9
                height = 9
                numMines = 10
                useBigState = false
            }
        }

        game.initBoard(width, height, numMines, useBigState)

        var alpha: Double = 0
        var gamma: Double = 0
        var epsilon: Double = 0

        println("\nAccepting algorithm parameters...\n")

        // Accept input for algorithm parametres
        try {
            println("Please enter the desired alpha (learning rate): ")
            alpha = StdIn.readDouble()
            println("Please enter the desired gamma (discount rate): ")
            gamma = StdIn.readDouble()
            println("Please enter the desired epsilon (chance of random choices): ")
            epsilon = StdIn.readDouble()
        } catch {
            case e: Exception => {
                println("Error reading input, using default values instead (alpha = 0.9, gamma = 0.7, epsilon = 0.3)")
                alpha = 0.9
                gamma = 0.7
                epsilon = 0.3
            }
        }

        println("\nInitialising Algorithm...\n")
        algorithm.initAlgorithm(game, alpha, gamma, epsilon)

        var iterations = 2500

        // Accept input for the number of iterations.
        try {
            println("Please enter the desired number of iterations: ")
            iterations = StdIn.readInt()
        } catch {
            case e: Exception => {
                println("Could not read the number of iterations. Defaulting to 2500.")
                iterations = 2500
            }
        }

        println("Games won in total: " + algorithm.beginLearning(iterations)._1)
    }
}
