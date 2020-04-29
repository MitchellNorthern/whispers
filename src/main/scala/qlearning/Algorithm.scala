package qlearning

import java.awt.Point

import game.GameBoard

import scala.collection.mutable.ListBuffer
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
    var epsilon: Double = 1

    /**
     * The rate at which epsilon decays. Decrements every time a choice is made.
     */
    val epsilonDecayRate = 0.05

    /**
     * A table containing all the state and possible actions with their weights.
     * To properly store the data, a list of tuples is necessary.
     * The first item of the tuple is the state. A "state" in this case is a list of the possible
     * surrounding tiles for each tile on the board. Only one instance of a possible state is necessary.
     * The possible action is to click the tile (the first double), and the value is the expected value of clicking it.
     *
     */
    var Q: ListBuffer[(List[Character], Double)] = null

    // The game board, necessary for a few calculations
    var board: GameBoard = null

    // The maximum possible reward for the game, based on the size of the game board and the number of mines
    var maxReward: Double = 0

    // A random number generator used for various things in the algorithm
    val random: Random = new Random()

    /**
     * Initialises the Q Learning algorithm.
     *
     * @param gameState - The game board as it starts.
     */
    def initAlgorithm(gameState: GameBoard): Unit = {
        // Set the game board and the max reward
        this.board = gameState
        this.maxReward = (this.board.w * this.board.h - this.board.numberOfMines) + 1000

        // Initialise the Q table
        convertBoardToState(this.board)
    }

    /**
     * Causes the agent to start learning Minesweeper.
     *
     * @param iterations - The number of iterations for the agent to run.
     * @return a tuple containing the number of games won, number of games lost, and the ending Q table
     */
    def beginLearning(iterations: Integer): (Integer, Integer, ListBuffer[(List[Character], Double)]) = {
        // Initialise needed variables to keep track of how many times we've played and won/lost
        var count = 0
        var gamesWon = 0
        var gamesLost = 0

        // Learn for the specified number of iterations
        while (count < iterations) {
            // Pause between iterations so humans can see what's going on.
            Thread.sleep(1000)

            // Print the board
            this.board.printBoard()

            // Make a choice and save the resulting tuple of the choice and the reward from it
            val choiceMadeTuple = makeChoice()

            // Figure out if we won or lost the game based on the reward
            val lostGame = choiceMadeTuple._2 == -1000
            val wonGame = choiceMadeTuple._2 == 1000

            // Update the Q value in the Q table for the action the agent picked
            this.Q = updateQ(choiceMadeTuple._1, choiceMadeTuple._2)

            // If we either won or lost the game, go to the next iteration with a new board
            if (lostGame || wonGame) {
                count += 1
                val tempBoard = new GameBoard
                tempBoard.initBoard(this.board.w, this.board.h, this.board.numberOfMines)
                this.board = tempBoard

                // Increment our games won or lost as necessary
                if (lostGame) gamesLost += 1
                if (wonGame) gamesWon += 1

                println(count)
            }
        }
        (gamesWon, gamesLost, this.Q)
    }

    /**
     * Updates the Q table based on the Q-Learning algorithm.
     */
    def updateQ(state: List[Character], reward: Integer): ListBuffer[(List[Character], Double)] = {
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
     * @return a tuple containing the state acted upon and the reward from the choice made
     */
    def makeChoice(): (List[Character], Integer) = {
        // Sort the states by the expected reward
        val sortedStates = this.Q.sortBy(_._2)
        println(sortedStates)

        // Determine whether or not we're exploring new options
        val exploring = random.nextDouble() < this.epsilon

        // If we're exploring, choose a random action. Otherwise, go for the max reward option.
        var choiceIndex = if (exploring) random.nextInt(sortedStates.size) else sortedStates.size - 1

        // After having chosen which state to look for (our action), find it.
        // If the state doesn't exist on the board, if you're exploring, randomly choose a new state to find.
        // If the state doesn't exist on the board and we're not exploring, choose the state with the next highest reward.
        var stateCoordinates: (Integer, Integer) = null
        while (stateCoordinates == null) {
            stateCoordinates = this.board.findState(sortedStates(choiceIndex)._1)
            if (stateCoordinates == null) {
                if (exploring) choiceIndex = random.nextInt(sortedStates.size) else choiceIndex -= 1
            }
        }
        // Return the state we looked for (our action) and the reward so we can update the Q table
        (sortedStates(choiceIndex)._1, this.board.handleChoice(stateCoordinates._1, stateCoordinates._2))
    }


    /**
     * Converts a game board to a list of list of characters.
     * Each list of characters within the list contains all the surrounding tiles for each tile.
     * In this way, most to all the possible states will be added to the Q table.
     * Duplicate lists are filtered out due to the state being a Hash Set.
     *
     * @param board - The game board to convert
     */
    def convertBoardToState(board: GameBoard): Unit = {
        // Do a double loop to get all the tiles
        for (i <- 0 to board.w) {
            for (j <- 0 to board.h) {
                // Get the game tile at this location
                val gameTile = board.board.get(new Point(i, j)).getOrElse(null)

                // Turn the surrounding tiles of the current tile into a piece of state.
                val newState = board.getSurroundingTiles(gameTile.x, gameTile.y)
                        .map(tile =>
                            if (tile != null) tile.display else ' '
                        ).filter(character =>
                            character != ' '
                        ).asInstanceOf[List[Character]]

                // If our new state isn't already known, add it to the Q table with a random expected reward.
                if (!this.Q.contains((newState, _))) this.Q.addOne(newState, this.random.nextDouble())
            }
        }
    }

}
