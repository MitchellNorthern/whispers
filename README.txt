# Whispers - A Minesweeper Solver
## Built for Honours Capstone at Southwest Baptist University
**To run the project, you must have Java installed.**

#### Running via the .jar
If you have been given the .jar file to run, please open a terminal of some type (if on Windows,
it's recommended to use Powershell as the following commands will likely work). Then run the jar from the terminal.
To do this...

1. Type `cd *name of the directory where the jar file is found*` without asterisks and hit enter.
For example, if your jar file is on your Desktop, you will likely type:
`cd C:/Users/*your username here*/Desktop/`
2. Type `java -jar *name of the jar without asterisks*` and hit enter to run the project.
For example, if the name of the project is `whispers-assembly-1.0`, the command to run it would be `java -jar whispers-assembly-1.0`

### Building the Project
You must have SBT installed to build the project.

Navigate to the root directory and run `sbt compile` to run the project.

Running `sbt assembly` will create a fat jar with all required dependencies (including Scala) that can be run standalone.
A terminal is required to run this jar as all output is sent to standard output. To run it using java, use `java -jar *jar name*`

Alternatively, using `sbt run` will also run the project.

### Using the Project
Follow the prompts to run the project. Make sure your hardware is decent, as the iterations required and the state size can potentially become massive.

#### The Prompts
##### Section 1: Board Size and State
The prompts here reference the size of the board, the number of mines on the board, and the state.
The state of the application refers to what will populate something called a Q Table. This Q Table is what
the application uses to learn. A piece of state in this application refers to a grid of either 3x3 or 5x5
size around any given unknown tile. The larger the state, the better-informed the bot will be when it makes
its decisions, but the longer it will take the bot to learn and the more memory the application will consume
when running. For such a reason, if your computer isn't built for machine learning algorithms, it is recommended
to use small state.

##### Section 2: Algorithm Parameters
Three algorithm parameters are used in this section that configure how the application will learn Minesweeper.
This is accomplished by changing three values: alpha, gamma, and epsilon. All three values decrease as the application learns.
<br/>
<br/>
**Note: All values are between 0 and 1 exclusive. That is, do not pick a value less than or greater than 0 or 1 respectively,
but also don't pick 0 or 1. The values and what they mean are described below as to provide better guidelines for picking values.**
###### Alpha
Alpha is the "learning rate" of the application. That is to say, the higher the alpha, the "faster" the application will learn.
The actual speed of the application is not affected, but fewer actions are required to tell the application whether or not an action
is a good or bad action to take.
###### Gamma
Gamma is the "discount rate" of the reward. That is, gamma determines how future-minded the application is. The higher the gamma, the
more long-term rewards are prioritised, meaning the algorithm will favour actions that provide long-term rewards.
###### Epsilon
Epsilon is the "exploration rate" of the application. The higher the epsilon, the more likely the
application is to take random actions (i.e. to "explore" or "try out" new actions).

##### Section 3: Number of Iterations
This section determines how many iterations the application will run for. The greater the number of iterations,
the more the agent will learn, but the more memory and time the application will take to run.

<br>

**Once you enter the number of iterations, the application will begin to run!**
