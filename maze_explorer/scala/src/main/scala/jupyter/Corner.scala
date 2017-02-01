package jupyter

import maze._

/**
  * Created by zak on 11/13/16.
  */
sealed abstract class Corner {
  def x: Int
  def y: Int
}

case object TopLeft extends Corner {
  def x = 0
  def y = 1
}

case object BottomLeft extends Corner {
  def x = 0
  def y = 0
}

case object TopRight extends Corner {
  def x = 1
  def y = 1
}

case object BottomRight extends Corner {
  def x = 1
  def y = 0
}

object Corner {
  def getWallCorners(wall: MazeWall): (Corner, Corner) = {
    val fieldsSeq = wall.fields.toSeq
    Direction(fieldsSeq.head, fieldsSeq.last) match {
      case North => (TopLeft, TopRight)
      case South => (BottomLeft, BottomRight)
      case West => (BottomLeft, TopLeft)
      case East => (BottomRight, TopRight)
    }
  }
}
