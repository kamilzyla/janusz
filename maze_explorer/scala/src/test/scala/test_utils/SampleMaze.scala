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
  private val N: Byte = 1
  private val E: Byte = 2
  private val S: Byte = 4
  private val W: Byte = 8

  private val fields = Array(
    S|W,    W|E,    W,      W,    W|N,
    S|N,    S|W,    E|N,    S|N,  S|N,
    S|N,    S,      W|N|E,  S|E,  N,
    S|N,    S|E|N,  S|W,    W|N,  S|N,
    S|E|N,  W|S|E,  N|E,    S|E,  N|E
  ).map(_.toByte)

  private val mazeReader = new MazeReader(numRows = 5, numColumns = 5)
  val maze = Maze("Sample maze", mazeReader.readMazeFields(fields))
}
