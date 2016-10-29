package maze

/**
  * Created by zak on 10/28/16.
  */
abstract class Direction {
  def dx: Int
  def dy: Int
}

final case class North() extends Direction {
  def dx = 0
  def dy = 1
}

final case class South() extends Direction {
  def dx = 0
  def dy = -1
}

final case class West() extends Direction {
  def dx = -1
  def dy = 0
}

final case class East() extends Direction {
  def dx = 1
  def dy = 0
}

object Direction {
  def apply(dx: Int, dy: Int): Direction = {
    (dx, dy) match {
      case (0, 1) => North()
      case (0, -1) => South()
      case (-1, 0) => West()
      case (1, 0) => East()
      case _ => throw new AssertionError(s"($dx, $dy) is not a valid direction")
    }
  }
}
