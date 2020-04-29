import game.GameBoard
import qlearning.Algorithm

/**
 * The entry point into the application.
 *
 * @author mnorthern
 */
object Main {
    def main(args: Array[String]): Unit = {
        val game: GameBoard = new GameBoard
        val algorithm: Algorithm = new Algorithm
        game.initBoard(30, 16, 99)
        algorithm.initAlgorithm(game)
        println(algorithm.beginLearning(1000))
    }
}
