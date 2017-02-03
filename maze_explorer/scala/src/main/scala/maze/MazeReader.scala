package maze

import java.io.File

import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.io.Source

/**
  * Created by zak on 10/27/16.
  */
class MazeReader(numRows: Int, numColumns: Int) {
  private val NorthByte: Byte = 1
  private val EastByte: Byte = 2
  private val SouthByte: Byte = 4
  private val WestByte: Byte = 8

  private val logger = LoggerFactory.getLogger("MazeReader")

  def readFromFile(file: File): Maze = {
    logger.debug("Read maze from file: " + file.getName)
    assert(file.exists())
    assert(file.isFile)

    val content = IOUtils.toByteArray(Source.fromFile(file).reader())
    assert(content.length == numRows * numColumns)

    val walls = readMazeWalls(content)
    Maze(file.getName, numRows = numRows, numColumns = numColumns, walls)
  }

  def readMazeWalls(content: Array[Byte]): Set[MazeWall] = {
    val mazeWallsSeq = for (x <- 0 until numRows; y <- 0 until numColumns)
      yield readFieldWalls(content, x, y)
    mazeWallsSeq.flatten.toSet
  }

  private def readFieldWalls(content: Array[Byte], x: Int, y: Int): Set[MazeWall] = {
    val fieldByte = content(x * numRows + y)
    fieldByteToSetOfWalls(MazeField(x, y), fieldByte)
  }

  private def fieldByteToSetOfWalls(field: MazeField, fieldByte: Byte): Set[MazeWall] = {
    val allWallBytes = NorthByte | EastByte | SouthByte | WestByte
    val hasOnlyWallBytesSet = (fieldByte | allWallBytes) == allWallBytes
    assert(hasOnlyWallBytesSet)

    def isWallInDirection(direction: Direction): Boolean = {
      val directionByte = direction match {
        case North => NorthByte
        case South => SouthByte
        case West => WestByte
        case East => EastByte
      }
      (fieldByte & directionByte) == directionByte
    }

    Direction.getAll
        .filter(isWallInDirection)
        .map { direction => MazeWall(field, direction) }
  }
}
