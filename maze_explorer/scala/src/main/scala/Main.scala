import java.io.File

import evaluation.LengthPlusTurns
import maze.{Maze, MazeReader}
import org.slf4j.LoggerFactory
import strategy.{Allowed, BellmanFord, Dfs, ExplorationStrategy}

/**
  * Created by zak on 10/27/16.
  */
object Main {
  val logger = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    val mazes = readMazes()
    //    val strategy = new Dfs()
    val strategy = new ExplorationStrategy(
      getNextField = new BellmanFord(
        transitionCostWhenNoWall = 1d,
        transitionsWhenUnknownWall = Allowed(cost = 1.5d)
      ).getNextFiled
    )
    val evaluation = new LengthPlusTurns()

    // TODO (zak): Add paths sanity checks
    val paths = (mazes map { maze => maze -> strategy.explore(maze) }).toMap
    val evaluations = paths mapValues evaluation.evaluate
    evaluations foreach { case (maze, value) => println("%-40s %f".format(maze.name, value)) }

    // TODO (zak): Print paths stats
    val avg = evaluations.values.sum / evaluations.size
    println(s"Avg: $avg")
  }

  def readMazes(): Set[Maze] = {
    val mazesDir = new File(getClass.getResource("mazes").getPath)
    assert(mazesDir.exists())
    assert(mazesDir.isDirectory)

    val mazeReader = new MazeReader(numRows = 16, numColumns = 16)
    val mazes = mazesDir.listFiles()
        .filter(_.isFile())
        .map(mazeReader.readFromFile)
        .toSet

    logger.info(s"Read ${mazes.size} mazes")
    mazes
  }
}
