import java.io.File

import evaluation.LengthPlusTurns
import maze.{Maze, MazeReader}
import org.slf4j.LoggerFactory
import strategy.Dfs

/**
  * Created by zak on 10/27/16.
  */
object Main {
  val logger = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    val mazes = readMazes()

    val strategy = new Dfs()
    val evaluation = new LengthPlusTurns()

    val paths = mazes.map(strategy.explore)
    paths foreach { path => println(evaluation.evaluate(path)) }
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
