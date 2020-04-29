package qlearning

import java.awt.Point
import java.util

import game.GameBoard

import scala.util.Random

class Algorithm() {

    /**
     * The learning rate.
     * The lower the alpha, the slower the less the algorithm changes its values.
     * As the agent learns, this value should decrease. This is because the agent needs to make less drastic alterations.
     */
    var alpha: Double = 0.4

    /**
     * The amount short-term rewards are prioritised over long-term rewards.
     * The lower the gamma, the more short term rewards are prioritised.
     * As the agent learns, the gamma decreases. This is because the agent is closer to the goal, so is thinking more short-term.
     */
    var gamma: Double = 0.5

    /**
     * The frequency at which exploration occurs.
     * The lower the epsilon, the less exploration.
     * As the agent learns, the epsilon decreases. This is because the agent can rely on previous experience more.
     */
    var epsilon: Double = 0.7

    /**
     * A table containing all the state and possible actions with their weights.
     * To properly store the data, a list of tuples is necessary.
     * The first item of the tuple is the state. A "state" in this case is a list of the possible
     * surrounding tiles for each tile on the board. Only one instance of a possible state is necessary.
     * The possible action is to click the tile (the first double), and the value is the expected value of clicking it.
     *
     */
    var Q: List[(List[Character], Double)] = null

    // The game board, necessary for a few calculations
    var board: GameBoard = null

    // The maximum possible reward for the game, based on the size of the game board and the number of mines
    var maxReward: Double = 0

    /**
     * Initialises the Q Learning algorithm.
     *
     * @param gameState - The game board as it starts.
     */
    def initAlgorithm(gameState: GameBoard): Unit = {
        // Set the game board and the max reward
        this.board = gameState
        this.maxReward = (this.board.w * this.board.h - this.board.numberOfMines) + 1000

        // Get the initial State values
        val initialStates = convertBoardToState(gameState, new util.HashSet[List[Character]]())

        // Set up a random number generator to initialise Q values
        val random = new Random()

        // Turn the HashSet into a Scala list for easier manipulation
        val initialQ = initialStates.toArray.toList.asInstanceOf[List[List[Character]]]

        // Set the initial states in the Q table. Values for expected rewards are random when first initialising the algorithm.
        this.Q = initialQ.map(item => (item, random.nextDouble()))
    }

    /**
     * Causes the agent to start learning Minesweeper.
     *
     * @param iterations - The number of iterations for the agent to run.
     * @return a tuple containing the number of games won, number of games lost, and the ending Q table
     */
    def beginLearning(iterations: Integer): (Integer, Integer, List[(List[Character], Double)]) = {
        var count = 0
        var gamesWon = 0
        var gamesLost = 0
        while (count < iterations) {
            val choiceMadeTuple = makeChoice()
            val lostGame = choiceMadeTuple._2 == -1000
            val wonGame = choiceMadeTuple._2 == 1000
            this.Q = updateQ(choiceMadeTuple._1, choiceMadeTuple._2)
            if (lostGame || wonGame) count += 1
            if (lostGame) gamesLost += 1
            if (wonGame) gamesWon += 1
        }
        (gamesWon, gamesLost, this.Q)
    }

    /**
     * Returns an updated Q table based on the Q-Learning algorithm.
     *
     * @return a new Q table with updated state and expected reward values
     */
    def updateQ(state: List[Character], reward: Integer): List[(List[Character], Double)] = {
        this.Q.map(item => {
            if (item._1.equals(state)) {
                (item._1, alpha * (reward + (gamma * maxReward) - item._2))
            } else {
                item
            }
        })
    }

    /**
     * Makes a choice as to what tile to click and clicks it.
     * Returns the reward from the choice made. If multiple tiles are revealed
     * by a cascade, the reward is the sum of revealing all the tiles.
     *
     * @return a tuple containing and the reward from the choice made
     */
    def makeChoice(): (List[Character], Integer) = {
        val sortedStates = this.Q.sortBy(_._2)
        var choiceIndex = sortedStates.size
        var stateCoordinates: (Integer, Integer) = null
        while (stateCoordinates == null) {
            stateCoordinates = this.board.findState(sortedStates(choiceIndex)._1)
            if (stateCoordinates == null) {
                choiceIndex -= 1
            }
        }
        (sortedStates(choiceIndex)._1, this.board.handleChoice(stateCoordinates._1, stateCoordinates._2))
    }


    /**
     * Converts a game board to a list of list of characters.
     * Each list of characters within the list contains all the surrounding tiles for each tile.
     * In this way, most to all the possible states will be added to the Q table.
     * Duplicate lists are filtered out due to the state being a Hash Set.
     *
     * @param board - The game board to convert
     * @param existingState - The current known states
     * @return a set containing all the states that exist or have existed in this or any game previous
     */
    def convertBoardToState(board: GameBoard, existingState: util.HashSet[List[Character]]): util.HashSet[List[Character]] = {
        // Create an array that contains the surrounding tiles of each tile. This is our state.
        val statesList = existingState

        // Do a double loop to get all the tiles
        for (i <- 0 to board.w) {
            for (j <- 0 to board.h) {
                // Get the game tile at this location
                val gameTile = board.board.get(new Point(i, j)).getOrElse(null)
                // Set the location in the array to a piece of state by getting the
                // tiles surrounding our current one and getting their 'display' property. Filter out unknown tiles.
                statesList :: board.getSurroundingTiles(gameTile.x, gameTile.y)
                        .map(tile =>
                            if (tile != null) tile.display else ' '
                        ).filter(character =>
                            if (character == ' ') false else true
                        )
            }
        }
        statesList
    }

}
