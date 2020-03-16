package game

import java.awt.Point

import scala.collection.mutable
import scala.util.Random

class GameBoard {

    var board: mutable.HashMap[Point, GameTile] = new mutable.HashMap[Point, GameTile]()

    def initBoard(width: Integer, height: Integer, numOfMines: Integer): Unit = {
        val generator = new Random(System.currentTimeMillis())

        var mines = numOfMines
        while (mines > 0) {
            val x = generator.nextInt(width)
            val y = generator.nextInt(height)
            if (canPlaceMine(x, y)) {
                mines -= 1
                this.board += (new Point(x, y) -> new GameTile(tileType = 'M'))
            }
        }
        for (i <- 0 to width) {
            for (j <- 0 to height) {
                val currentTile = this.board.get(new Point(i, j)).getOrElse(null)
                if (currentTile == null) {
                    val surroundingMines = getMineCount(i, j)
                    var tile: Character = ' '
                    if (surroundingMines > 0) {
                        tile = (surroundingMines.toString).charAt(0)
                    }
                    this.board += (new Point(i, j) -> new GameTile(tileType = tile))
                }
            }
        }
    }

    def canPlaceMine(x: Integer, y: Integer): Boolean = {
        getMineCount(x, y) < 8
    }

    def getMineCount(x: Integer, y: Integer): Integer = {
        var count = 0
        val surroundingTiles: List[GameTile] = getSurroundingTiles(x, y)
        for (tile <- surroundingTiles) {
            if (tile != null && tile.tileType == 'M') {
                count += 1
            }
        }
        count
    }

    def getSurroundingTiles(x: Integer, y: Integer): List[GameTile] = {
        val left = this.board.get(new Point(x - 1, y)).getOrElse(null)
        val right = this.board.get(new Point(x + 1, y)).getOrElse(null)
        val top = this.board.get(new Point(x, y + 1)).getOrElse(null)
        val bottom = this.board.get(new Point(x, y - 1)).getOrElse(null)
        val topRight = this.board.get(new Point(x + 1, y + 1)).getOrElse(null)
        val topLeft = this.board.get(new Point(x - 1, y + 1)).getOrElse(null)
        val bottomRight = this.board.get(new Point(x + 1, y - 1)).getOrElse(null)
        val bottomLeft = this.board.get(new Point(x - 1, y - 1)).getOrElse(null)

        List(left, right, top, bottom, topRight, topLeft, bottomRight, bottomLeft)
    }

    def handleChoice(x: Integer, y: Integer): Unit = {

    }

    def cascadeClick(x: Integer, y: Integer): Unit = {

    }

    def printBoard(width: Integer, height: Integer, revealed: Boolean): Unit = {
        for (num <- 0 to (width + 2)) {
            print("+")
        }
        for (h <- (height - 1) to 0 by -1) {
            println()
            print("+")
            for (w <- 0 to width) {
                val currentTile = this.board.get(new Point(w, h)).get
                if (revealed) {
                    print(currentTile.tileType.toString)
                } else {
                    print(currentTile.display.toString)
                }
            }
            print("+")
        }
        println()
        for (num <- 0 to (width + 2)) {
            print("+")
        }
        // Print once more to flush
        println()
    }

}
