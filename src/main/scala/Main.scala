import Printer.Printer

import scala.io.StdIn

/**
 * The entry point into the Whispers application.
 *
 * @author mnorthern
 */
object Main {
    def main(args: Array[String]): Unit = {
        printBanner()
        Thread.sleep(3000)

        var gameStarted: Boolean = false
        while (!gameStarted) {
            printOptions()
            var input: Int = 0
            try {
                input = StdIn.readInt()
            } catch {
                case _: Exception => println("Invalid input. Please enter a number between 1 and 3")
            }
            input match {
                case 1 => gameStarted = true
                case 2 => println("Placeholder")
                case 3 => System.exit(0)
                case _ => println("Please enter a number between 1 and 3")
            }
        }

        // The game has begun
    }

    def printBanner(): Unit = {
        println("Perpare yourself, for you hear from the darkness...")
        println("__        ___     _                         \n\\ \\      / / |__ (_)___ _ __   ___ _ __ ___ \n \\ \\ /\\ / /| '_ \\| / __| '_ \\ / _ \\ '__/ __|\n  \\ V  V / | | | | \\__ \\ |_) |  __/ |  \\__ \\\n   \\_/\\_/  |_| |_|_|___/ .__/ \\___|_|  |___/\n                       |_|        ")
    }

    def printOptions(): Unit = {
        val start: String = Printer.getAsColor("1. Start", "GREEN")
        val options: String = Printer.getAsColor("2. Options", "BLUE")
        val exit: String = Printer.getAsColor("3. Exit", "RED")
        println(start)
        println(options)
        println(exit)

    }
}
