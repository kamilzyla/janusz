package maze

import java.awt.{Color, Paint, Shape}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import jupyter.MazeDrawer

/**
  * Created by zak on 10/27/16.
  */

sealed abstract class Wall

final case class NorthWall() extends Wall

final case class SouthWall() extends Wall

final case class WestWall() extends Wall

final case class EastWall() extends Wall

case class MazeField(x: Int, y: Int, walls: Set[Wall])

case class Maze(name: String, fields: Array[Array[MazeField]]) {
  val startField = fields(0)(0)

  val numRows = fields.length
  assert(numRows > 0)

  val numColumns = fields.head.length

  assert(areFieldsConsistent())

  def getNeighbours(field: MazeField): Set[MazeField] = {
    Direction.getAll
        .filter(isInside(field, _))
        .map(direction => {
          val newField = getNeighbour(field, direction)
          if (isWallBetween(field, newField)) None else Some(newField)
        })
        .filter(_.isDefined)
        .map(_.get)
  }

  def generateHtmlToDisplay: String = {
    val file = new File("imgs/" + name + ".png")
    val img = MazeDrawer.draw(this)

    file.mkdirs()
    ImageIO.write(img, "png", file)

    "<img src=\"" + file.getPath + "\" />"
  }

  private def getNeighbour(field: MazeField, direction: Direction): MazeField = {
    assert(isInside(field, direction))
    fields(field.x + direction.dx)(field.y + direction.dy)
  }

  private def isInside(field: MazeField, direction: Direction): Boolean = {
    val newRow = field.x + direction.dx
    val newColumn = field.y + direction.dy
    0 <= newRow && newRow < numRows && 0 <= newColumn && newColumn < numColumns
  }


  private def isWallBetween(firstField: MazeField, secondField: MazeField): Boolean = {
    Direction(firstField, secondField) match {
      case North() => firstField.walls contains NorthWall()
      case South() => firstField.walls contains SouthWall()
      case West() => firstField.walls contains WestWall()
      case East() => firstField.walls contains EastWall()
    }
  }

  private def areFieldsConsistent(): Boolean = {
    fields.flatten forall isFieldConsistentWithNeighbours
  }

  private def isFieldConsistentWithNeighbours(field: MazeField): Boolean = {
    Direction.getAll
        .filter(isInside(field, _))
        .forall(direction => {
          val newField = getNeighbour(field, direction)
          (isWallBetween(field, newField) && isWallBetween(newField, field)) ||
              (!isWallBetween(field, newField) && !isWallBetween(newField, field))
        })
  }
}

