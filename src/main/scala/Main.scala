import game.GameBoard

/**
 * The entry point into the application.
 *
 * @author mnorthern
 */
object Main {
    def main(args: Array[String]): Unit = {
        val game: GameBoard = new GameBoard
        game.initBoard(30, 16, 99)
        while (!game.lost) {
            game.printBoard()
            print("X: ")
            val x: Integer = scala.io.StdIn.readInt()
            println()
            print("Y: ")
            val y: Integer = scala.io.StdIn.readInt()
            println()

            game.handleChoice(x, y)

        }
    }
}
