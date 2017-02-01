package maze

import java.io.File
import javax.imageio.ImageIO

import jupyter.MazeDrawer

/**
  * Created by zak on 10/27/16.
  */

case class MazeWall(fields: Set[MazeField]) {
  assert(fields.size == 2)
}

object MazeWall {
  def apply(firstField: MazeField, secondField: MazeField): MazeWall = MazeWall(Set(firstField, secondField))

  def apply(field: MazeField, direction: Direction): MazeWall = {
    val neighbour = Maze.getNeighbour(field, direction)
    MazeWall(field, neighbour)
  }
}

case class MazeField(x: Int, y: Int)

case class Maze(name: String, numRows: Int, numColumns: Int, walls: Set[MazeWall]) {
  assert(numRows > 0)
  assert(numColumns > 0)

  val startField = MazeField(0, 0)

  val centralFields = (Set(numRows / 2, (numRows + 1) / 2) zip Set(numColumns / 2, (numColumns + 1) / 2))
      .map { case (row, col) => MazeField(row, col) }

  def getNeighbours(field: MazeField): Set[MazeField] = {
    Direction.getAll
        .filter(!isWall(field, _))
        .map(Maze.getNeighbour(field, _))
  }

  def getWalls(field: MazeField): Set[MazeWall] =
    getNeighbours(field).map(MazeWall(field, _))

  def generateHtmlToDisplay: String = {
    val file = new File("imgs/" + name + ".png")
    val img = MazeDrawer.draw(this)

    file.mkdirs()
    ImageIO.write(img, "png", file)

    "<img src=\"" + file.getPath + "\" />"
  }

  private def isInside(field: MazeField, direction: Direction): Boolean = {
    val newRow = field.x + direction.dx
    val newColumn = field.y + direction.dy
    0 <= newRow && newRow < numRows && 0 <= newColumn && newColumn < numColumns
  }

  private def isWall(field: MazeField, direction: Direction): Boolean =
    walls contains MazeWall(field, direction)

  private def isWallBetween(firstField: MazeField, secondField: MazeField): Boolean =
    walls contains MazeWall(firstField, secondField)
}

object Maze {
  def getNeighbour(field: MazeField, direction: Direction): MazeField = {
    MazeField(field.x + direction.dx, field.y + direction.dy)
  }
}
