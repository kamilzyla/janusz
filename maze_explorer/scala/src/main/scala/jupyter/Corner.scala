package jupyter

import maze._

/**
  * Created by zak on 11/13/16.
  */
sealed abstract class Corner {
  def x: Int
  def y: Int
}

case class TopLeft() extends Corner {
  def x = 0
  def y = 1
}

case class BottomLeft() extends Corner {
  def x = 0
  def y = 0
}

case class TopRight() extends Corner {
  def x = 1
  def y = 1
}

case class BottomRight() extends Corner {
  def x = 1
  def y = 0
}

object Corner {
  def getWallCorners(wall: Wall): (Corner, Corner) = {
    wall match {
      case NorthWall() => (TopLeft(), TopRight())
      case SouthWall() => (BottomLeft(), BottomRight())
      case WestWall() => (BottomLeft(), TopLeft())
      case EastWall() => (BottomRight(), TopRight())
    }
  }
}
