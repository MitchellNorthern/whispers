package game

import java.awt.Point

import scala.collection.mutable
import scala.util.Random

/**
 * An instance of Minesweeper
 *
 * @author mnorthern
 */
class GameBoard {

    var board: mutable.HashMap[Point, GameTile] = new mutable.HashMap[Point, GameTile]()
    var lost: Boolean = false
    var w: Integer = 30
    var h: Integer = 16
    var numberOfMines: Integer = 99
    var numberOfOtherTiles: Integer = 381

    /**
     * Initialises the board with the given parametres.
     *
     * @param width - The width of the board
     * @param height - The height of the board
     * @param numOfMines - The number of mines the board should have
     */
    def initBoard(width: Integer, height: Integer, numOfMines: Integer): Unit = {
        val generator = new Random(System.currentTimeMillis())

        this.w = width
        this.h = height
        this.numberOfMines = numOfMines

        // If too many mines are desired, leave one tile open
        if (this.numberOfMines >= this.w * this.h) this.numberOfMines = this.w * this.h - 1

        this.numberOfOtherTiles = this.w * this.h - this.numberOfMines

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

    /**
     * Determines whether or not a mine can be placed in a specific spot based on the number of mines around it.
     *
     * @param x - The x coordinate of the desired spot
     * @param y - The y coordinate of the desired spot
     * @return True if a mine can be placed at the spot, else false
     */
    def canPlaceMine(x: Integer, y: Integer): Boolean = {
        getMineCount(x, y) < 8
    }

    /**
     * Gets the number of mines touching a tile.
     *
     * @param x - The x coordinate of the tile
     * @param y - The y coordinate of the tile
     * @return The number of mines
     */
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

    /**
     * Gets the tiles surrounding a specific tile.
     *
     * @param x - The x coordinate of the tile
     * @param y - The y coordinate of the tile
     * @return A list of tiles surrounding a given tile
     */
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

    /**
     * Gets the state for a given tile. This is a 5x5 square around said tile.
     *
     * @param x - The x coordinate of the tile
     * @param y - The y coordinate of the tile
     * @return A list of Game Tiles that make up that state.
     */
    def getState(x: Integer, y: Integer): List[GameTile] = {

        // The below code requires a great deal of computational power to run at any reasonable speed.
        // Thus, a 3x3 square is used instead. This can be added back in if running with more computing power.s
        /*
        val left = this.board.get(new Point(x - 1, y)).getOrElse(null)
        val right = this.board.get(new Point(x + 1, y)).getOrElse(null)
        val top = this.board.get(new Point(x, y + 1)).getOrElse(null)
        val bottom = this.board.get(new Point(x, y - 1)).getOrElse(null)
        val topRight = this.board.get(new Point(x + 1, y + 1)).getOrElse(null)
        val topLeft = this.board.get(new Point(x - 1, y + 1)).getOrElse(null)
        val bottomRight = this.board.get(new Point(x + 1, y - 1)).getOrElse(null)
        val bottomLeft = this.board.get(new Point(x - 1, y - 1)).getOrElse(null)
        val farLeft = this.board.get(new Point(x - 2, y)).getOrElse(null)
        val farLeftUp = this.board.get(new Point(x - 2, y + 1)).getOrElse(null)
        val farLeftDown = this.board.get(new Point(x - 2, y - 1)).getOrElse(null)
        val farRight = this.board.get(new Point(x + 2, y)).getOrElse(null)
        val farRightUp = this.board.get(new Point(x + 2, y + 1)).getOrElse(null)
        val farRightDown = this.board.get(new Point(x + 2, y - 1)).getOrElse(null)
        val farTop = this.board.get(new Point(x, y + 2)).getOrElse(null)
        val farTopLeft = this.board.get(new Point(x - 1, y + 2)).getOrElse(null)
        val farTopRight = this.board.get(new Point(x + 1, y + 2)).getOrElse(null)
        val farBottom = this.board.get(new Point(x, y - 2)).getOrElse(null)
        val farBottomLeft = this.board.get(new Point(x - 1, y - 2)).getOrElse(null)
        val farBottomRight = this.board.get(new Point(x + 1, y - 2)).getOrElse(null)
        val topLeftCorner = this.board.get(new Point(x - 2, y + 2)).getOrElse(null)
        val topRightCorner = this.board.get(new Point(x + 2, y + 2)).getOrElse(null)
        val bottomLeftCorner = this.board.get(new Point(x - 2, y - 2)).getOrElse(null)
        val bottomRightCorner = this.board.get(new Point(x + 2, y - 2)).getOrElse(null)

        List(left, right, top, bottom, topRight, topLeft, bottomRight, bottomLeft, farLeft, farLeftUp, farLeftDown,
            farRight, farRightUp, farRightDown, farTop, farTopLeft, farTopRight, farBottom, farBottomLeft, farBottomRight,
            topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner
        )
        */

        getSurroundingTiles(x, y)
    }

    /**
     * Finds an instance of state and returns the x and y coordinates where to find it on the board.
     *
     * @param state - The state to find.
     */
    def findState(state: List[Character]): (Integer, Integer) = {
        for (i <- 0 to this.w) {
            for (j <- 0 to this.h) {
                // Get the game tile at this location
                val gameTile = board.get(new Point(i, j)).getOrElse(null)
                if (gameTile != null && gameTile.display == '-') {
                    val surroundingTileDisplays: List[Character] = getState(gameTile.x, gameTile.y)
                        .map(item => if (item != null) item.display else '+')
                        .asInstanceOf[List[Character]]
                    if (state.equals(surroundingTileDisplays)) {
                        return (gameTile.x, gameTile.y)
                    }
                }
            }
        }
        null
    }

    /**
     * Handles a choice of a tile to click on.
     *
     * @param x - The x coordinate of the tile clicked.
     * @param y - The y coordiante of the tile clicked.
     * @return The reward to give the learning agent.
     */
    def handleChoice(x: Integer, y: Integer): Integer = {
        val selectedTile = this.board.get(new Point(x, y)).get
        if (selectedTile.tileType == 'M') {
            this.lost = true
            revealAllMines()
            -100
        } else {
            val reward = cascadeClick(selectedTile, x, y)
            if (checkWon()) 100 else reward
        }
    }

    /**
     * Determines if the game has been won based on the number of tiles clicked that are not mines
     *
     * @return True if the game has been won, else false
     */
    def checkWon(): Boolean = {
        this.numberOfOtherTiles == 0
    }

    /**
     * Cascades a click to surrounding tiles that have no adjacent mines.
     *
     * @param tile - The tile clicked. Cannot be a mine.
     * @param x - The x coordinate of the tile
     * @param y - The y coordinate of the tile
     * @return The reward to give the agent for clicking on a tile.
     */
    def cascadeClick(tile: GameTile, x: Integer, y: Integer): Integer = {
        if (tile.tileType == ' ' && tile.display != ' ') {
            tile.revealTile()
            val surroundingTiles: List[GameTile] = getSurroundingTiles(x, y)
            surroundingTiles.map(surroundingTile => {
                if (surroundingTile != null) {
                    cascadeClick(surroundingTile, surroundingTile.x, surroundingTile.y)
                }
            })
        } else if (tile.tileType != 'M') {
            tile.revealTile()
        }
        this.numberOfOtherTiles -= 1
        1
    }

    /**
     * Displays all the mines in a game.
     */
    def revealAllMines(): Unit = {
        for (h <- (this.h - 1) to 0 by -1) {
            for (w <- 0 to this.w) {
                val currentTile = this.board.get(new Point(w, h)).get
                if (currentTile.tileType == 'M') {
                    currentTile.revealTile
                }
            }
        }
    }

    /**
     * Prints out the game board.
     */
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
