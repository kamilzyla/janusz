package maze

/**
  * Created by zak on 10/28/16.
  */
sealed abstract class Direction {
  def dx: Int
  def dy: Int
}

case object North extends Direction {
  def dx = 0
  def dy = 1
}

case object South extends Direction {
  def dx = 0
  def dy = -1
}

case object West extends Direction {
  def dx = -1
  def dy = 0
}

case object East extends Direction {
  def dx = 1
  def dy = 0
}

object Direction {
  def apply(dx: Int, dy: Int): Direction = {
    (dx, dy) match {
      case (0, 1) => North
      case (0, -1) => South
      case (-1, 0) => West
      case (1, 0) => East
      case _ => throw new AssertionError(s"($dx, $dy) is not a valid direction")
    }
  }

  def apply(firstField: MazeField, secondField: MazeField): Direction = {
    val dx = secondField.x - firstField.x
    val dy = secondField.y - firstField.y
    Direction(dx, dy)
  }

  def getAll: Set[Direction] = Set(North, South, West, East)
}
