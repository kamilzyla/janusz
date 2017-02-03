package test_utils

import maze._

/**
  * Created by zak on 10/29/16.
  */

//  _ _ _ _ _
// |  _   _  |
// |  _ _|  _|
// |   | |_  |
// | |_ _ _|_|
// |_ _ _ _ _|
//

object SampleMaze {
  val numRows = 5
  val numColumns = 5

  private val N: Byte = 1
  private val E: Byte = 2
  private val S: Byte = 4
  private val W: Byte = 8

  val fieldsBytes = Array(
    S|W,    W|E,    W,      W,    W|N,
    S|N,    S|W,    E|N,    S|N,  S|N,
    S|N,    S,      W|N|E,  S|E,  N,
    S|N,    S|E|N,  S|W,    W|N,  S|N,
    S|E|N,  W|S|E,  N|E,    S|E,  N|E
  ).map(_.toByte)

  private val mazeReader = new MazeReader(numRows = numRows, numColumns = numColumns)
  val maze = Maze(
    name = "Sample maze",
    numRows = numRows,
    numColumns = numColumns,
    walls = mazeReader.readMazeWalls(fieldsBytes)
  )
}
