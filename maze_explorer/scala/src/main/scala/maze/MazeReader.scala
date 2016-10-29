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

  def readFromFile(file: File): Option[Maze] = {
    logger.debug("Read maze from file: " + file.getName)
    assert(file.exists())
    assert(file.isFile)

    val content = IOUtils.toByteArray(Source.fromFile(file).reader())
    if (content.length == numRows * numColumns) {
      val fields = readMazeFields(content)
      Some(Maze(file.getName, fields))
    }
    else {
      logger.warn(s"File '${file.getName}': invalid length: ${content.length}")
      None
    }
  }

  def readMazeFields(content: Array[Byte]): Array[Array[MazeField]] = {
    val fields = Array.ofDim[MazeField](numRows, numColumns)
    for (y <- 0 until numRows; x <- 0 until numColumns) {
      fields(x)(y) = readField(content, x, y)
    }
    fields
  }

  private def readField(content: Array[Byte], x: Int, y: Int): MazeField = {
    val fieldByte = content(x * numRows + y)
    MazeField(x, y, fieldByteToSetOfWalls(fieldByte))
  }

  private def fieldByteToSetOfWalls(fieldByte: Byte): Set[Wall] = {
    val allWallBytes = NorthByte | EastByte | SouthByte | WestByte
    val hasOnlyWallBytesSet = (fieldByte | allWallBytes) == allWallBytes
    assert(hasOnlyWallBytesSet)

    def maybeWall(wallByte: Byte, wall: Wall): Option[Wall] =
        if ((fieldByte & wallByte) == wallByte) Some(wall) else None

    Set(
      maybeWall(NorthByte, NorthWall()),
      maybeWall(SouthByte, SouthWall()),
      maybeWall(WestByte, WestWall()),
      maybeWall(EastByte, EastWall())
    ).filter(_.isDefined).map(_.get)
  }
}
