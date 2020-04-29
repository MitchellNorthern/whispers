package game

import java.awt.Point

import scala.collection.mutable
import scala.util.Random

class GameBoard {

    var board: mutable.HashMap[Point, GameTile] = new mutable.HashMap[Point, GameTile]()
    var lost: Boolean = false
    var w: Integer = 30
    var h: Integer = 16
    var numberOfMines: Integer = 99

    def initBoard(width: Integer, height: Integer, numOfMines: Integer): Unit = {
        val generator = new Random(System.currentTimeMillis())

        this.w = width
        this.h = height
        this.numberOfMines = numOfMines

        var mines = numOfMines
        while (mines > 0) {
            val x = generator.nextInt(width)
            val y = generator.nextInt(height)
            if (canPlaceMine(x, y)) {
                mines -= 1
                this.board += (new Point(x, y) -> new GameTile(tileType = 'M', x = x, y = y))
            }
        }
        for (i <- 0 to this.w) {
            for (j <- 0 to this.h) {
                val currentTile = this.board.get(new Point(i, j)).getOrElse(null)
                if (currentTile == null) {
                    val surroundingMines = getMineCount(i, j)
                    var tile: Character = ' '
                    if (surroundingMines > 0) {
                        tile = (surroundingMines.toString).charAt(0)
                    }
                    this.board += (new Point(i, j) -> new GameTile(tileType = tile, x = i, y = j))
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

    def findState(state: List[Character]): (Integer, Integer) = {
        for (i <- 0 to this.w) {
            for (j <- 0 to this.h) {
                // Get the game tile at this location
                val gameTile = board.get(new Point(i, j)).getOrElse(null)
                if (gameTile != null && gameTile.display == '-') {
                    val surroundingTileDisplays = getSurroundingTiles(gameTile.x, gameTile.y)
                        .map(item => if (item != null) item.display else ' ')
                        .filter(item => item != ' ')
                    if (state.equals(surroundingTileDisplays)) {
                        return (gameTile.x, gameTile.y)
                    }
                }
            }
        }
        null
    }

    def handleChoice(x: Integer, y: Integer): Integer = {
        val selectedTile = this.board.get(new Point(x, y)).get
        if (selectedTile.tileType == 'M') {
            this.lost = true
            revealAllMines()
            -1000
        } else {
            cascadeClick(selectedTile, x, y, 1)
        }
    }

    def cascadeClick(tile: GameTile, x: Integer, y: Integer, reward: Integer): Integer = {
        if (tile.tileType == ' ' && tile.display != ' ') {
            tile.revealTile()
            val surroundingTiles: List[GameTile] = getSurroundingTiles(x, y)
            surroundingTiles.map(surroundingTile => {
                if (surroundingTile != null) {
                    cascadeClick(surroundingTile, surroundingTile.x, surroundingTile.y, reward)
                }
            })
        } else if (tile.tileType != 'M') {
            tile.revealTile()
        }
        reward + 1
    }

    def revealAllMines(): Unit = {
        for (h <- (this.h - 1) to 0 by -1) {
            for (w <- 0 to this.w) {
                val currentTile = this.board.get(new Point(w, h)).get
                if (currentTile.tileType == 'M') {
                    currentTile.revealTile
                }
            }
        }
        printBoard()
    }

    def printBoard(): Unit = {
        for (num <- 0 to (this.w + 2)) {
            print("+")
        }
        for (h <- (this.h - 1) to 0 by -1) {
            println()
            print("+")
            for (w <- 0 to this.w) {
                val currentTile = this.board.get(new Point(w, h)).get
                print(currentTile.display.toString)
            }
            print("+")
        }
        println()
        for (num <- 0 to (this.w + 2)) {
            print("+")
        }
        // Print once more to flush
        println()
    }

}
