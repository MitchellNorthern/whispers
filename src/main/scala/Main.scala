import game.GameBoard

/**
 * The entry point into the Whispers application.
 *
 * @author mnorthern
 */
object Main {
    def main(args: Array[String]): Unit = {
        val game: GameBoard = new GameBoard
        game.initBoard(30, 16, 99)
        game.printBoard(30, 16, true)
    }
}
