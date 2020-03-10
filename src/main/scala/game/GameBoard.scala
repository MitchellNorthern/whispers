package game

import java.awt.Point
import java.util

import scala.util.Random

object GameBoard {

    var board: Map[Point, GameTile]

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
                if (this.board.get(new Point(i, j)) != null) {
                    this.board += (new Point(i, j) -> new GameTile(tileType = ''))
                }
            }
        }
    }

    def canPlaceMine(x: Integer, y: Integer): Boolean = {

    }

    def getMineCount(x: Integer, y: Integer): Integer = {
        var count = 0
        val surroundingTiles: List[GameTile] = getSurroundingTiles(x, y)
        for (tile <- surroundingTiles) {
            if (tile != null && tile.tileType != 'M') {
                count += 1
            }
        }
    }

    def getSurroundingTiles(x: Integer, y: Integer): List[GameTile] = {
        val left = this.board.get(new Point(x - 1, y))
        val right = this.board.get(new Point(x + 1, y))
        val top = this.board.get(new Point(x, y + 1))
        val bottom = this.board.get(new Point(x, y - 1))
        val topRight = this.board.get(new Point(x + 1, y + 1))
        val topLeft = this.board.get(new Point(x - 1, y + 1))
        val bottomRight = this.board.get(new Point(x + 1, y - 1))
        val bottomLeft = this.board.get(new Point(x - 1, y - 1))

        List(left, right, top, bottom, topRight, topLeft, bottomRight, bottomLeft)
    }

    def handleChoice(x: Integer, y: Integer): Unit = {

    }

    def cascadeClick(x: Integer, y: Integer): Unit = {

    }

    def printBoard(): Unit = {

    }

}
