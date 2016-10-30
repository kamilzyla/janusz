package maze

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

  def getNeighbours(field: MazeField): Set[MazeField] = {
    val directions = Set(North(), South(), West(), East())
    directions
        .filter(isInside(field, _))
        .map(direction => {
          val newField = move(field, direction)
          if (isWallBetween(field, newField)) None else Some(newField)
        })
        .filter(_.isDefined)
        .map(_.get)
  }

  private def move(field: MazeField, direction: Direction): MazeField = {
    assert(isInside(field, direction))
    fields(field.x + direction.dx)(field.y + direction.dy)
  }

  private def isInside(field: MazeField, direction: Direction): Boolean = {
    val newRow = field.x + direction.dx
    val newColumn = field.y + direction.dy
    0 <= newRow && newRow < numRows && 0 <= newColumn && newColumn < numColumns
  }


  private def isWallBetween(firstField: MazeField, secondField: MazeField): Boolean = {
    def dx = secondField.x - firstField.x
    def dy = secondField.y - firstField.y

    Direction(dx, dy) match {
      case North() => firstField.walls contains NorthWall()
      case South() => firstField.walls contains SouthWall()
      case West() => firstField.walls contains WestWall()
      case East() => firstField.walls contains EastWall()
    }
  }
}

