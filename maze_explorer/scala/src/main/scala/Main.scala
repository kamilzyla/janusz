import java.io.File

import evaluation.LengthPlusTurns
import maze.{Maze, MazeReader}
import org.slf4j.LoggerFactory
import strategy.{Allowed, BellmanFord, ExplorationStrategy}

/**
  * Created by zak on 10/27/16.
  */
object Main {
  val logger = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    val mazes = if (args.isEmpty) readAllMazes() else readMazes(args.toSet)
    //    val strategy = new Dfs()
    def getFreshStrategy = new ExplorationStrategy(
      new BellmanFord(
        transitionCostWhenNoWall = 1d,
        transitionsWhenUnknownWall = Allowed(cost = 1.5d)
      )
    )
    val evaluation = new LengthPlusTurns()

    // TODO (zak): Add paths sanity checks
    val paths = (mazes map { maze => maze -> getFreshStrategy.explore(maze) }).toMap
    val evaluations = paths mapValues evaluation.evaluate
    evaluations foreach { case (maze, value) => println("%-40s %f".format(maze.name, value)) }

    // TODO (zak): Print paths stats
    val avg = evaluations.values.sum / evaluations.size
    println(s"Avg: $avg")
  }

  def readAllMazes(): Set[Maze] = {
    val mazesToRead = getAllMazesFiles.map(_.getName)
    readMazes(mazesToRead)
  }

  def readMazes(mazesToRead: Set[String]): Set[Maze] = {
    val mazeReader = new MazeReader(numRows = 16, numColumns = 16)
    val mazes = getAllMazesFiles
        .filter(file => mazesToRead contains file.getName)
        .map(mazeReader.readFromFile)

    logger.info(s"Read ${mazes.size} mazes")
    mazes
  }

  private def getAllMazesFiles: Set[File] = {
    val mazesDir = new File(getClass.getResource("mazes").getPath)
    assert(mazesDir.exists())
    assert(mazesDir.isDirectory)
    mazesDir.listFiles()
      .filter(_.isFile())
      .toSet
  }
}
