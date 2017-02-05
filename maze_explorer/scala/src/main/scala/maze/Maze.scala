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
  def apply(firstField: MazeField, secondField: MazeField): MazeWall =
    MazeWall(Set(firstField, secondField))

  def apply(field: MazeField, direction: Direction): MazeWall = {
    val neighbour = Maze.getPossibleNeighbour(field, direction)
    MazeWall(field, neighbour)
  }
}

case class MazeField(x: Int, y: Int)

case class Maze(name: String, numRows: Int, numColumns: Int, walls: Set[MazeWall]) {
  assert(numRows > 0)
  assert(numColumns > 0)

  val startField = MazeField(0, 0)

  def centralFields: Set[MazeField] = {
    def getCentral(x: Int) = Set((x - 1) / 2, x / 2)
    for (x <- getCentral(numRows); y <- getCentral(numColumns))
      yield MazeField(x, y)
  }

  def getNeighbours(field: MazeField): Set[MazeField] =
    Direction.getAll
        .filter(!isWall(field, _))
        .map(Maze.getPossibleNeighbour(field, _))

  def getWalls(field: MazeField): Set[MazeWall] =
    Direction.getAll
        .filter(isWall(field, _))
        .map(MazeWall(field, _))

  def generateVisualization: String = {
    val file = new File("imgs/" + name + ".png")
    val img = MazeDrawer.draw(this)

    file.mkdirs()
    ImageIO.write(img, "png", file)

    file.getAbsolutePath
  }

  def isInside(field: MazeField): Boolean =
    0 <= field.x && field.x < numColumns && 0 <= field.y && field.y < numRows

  private def isWall(field: MazeField, direction: Direction): Boolean =
    walls contains MazeWall(field, direction)
}

object Maze {
  def getPossibleNeighbour(field: MazeField, direction: Direction): MazeField = {
    MazeField(field.x + direction.dx, field.y + direction.dy)
  }
}
