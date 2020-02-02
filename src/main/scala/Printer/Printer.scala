package Printer

/**
 * Provides methods for printing to the console
 *
 * @author mnorthern
 */
object Printer {

    /**
     * Prints a string to the console at a delay
     * The default is set to 50 ms.
     *
     * <p>
     * Good delay times are:
     * <ul>
     *  <li>100 for slow printing</li>
     *  <li>50 for medium printing</li>
     *  <li>20 for fast printing</li>
     * </ul>
     * </p>
     * @param s     The string to print to the console
     */
    def printDelay(str: String, delay: Int = 50): Unit = {
        val brokenString: Array[String] = str.split("")
        for (elem <- brokenString) {
            Thread.sleep(delay)
            print(elem)
            System.out.flush()
        }
        println()
    }

    def getAsColor(str: String, color: String): String = {
        matchColor(color) + str + Console.RESET
    }

    private def matchColor(color: String): String = {
        val upperColor = color.toUpperCase()
        upperColor match {
            case "RED" => Console.RED
            case "BLUE" => Console.BLUE
            case "GREEN" => Console.GREEN
            case "YELLOW" => Console.YELLOW
            case _ => Console.RESET
        }
    }
}
