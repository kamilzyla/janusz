package evaluation

import maze.{Direction, MazeField, North}

/**
  * Created by zak on 10/30/16.
  */
class LengthPlusTurns extends Evaluation {
  override def evaluate(path: Seq[MazeField]): Double =
    (path.length + numTurns(path)).toDouble

  private def numTurns(path: Seq[MazeField]): Int = {
    val directions = (0 until path.length - 1) map { idx => Direction(path(idx), path(idx + 1)) }
    numTurns(directions, North)
  }

  private def numTurns(directions: Seq[Direction], currentDirection: Direction): Int = {
    directions match {
      case Seq() => 0
      case Seq(head, tail @ _*) if head == currentDirection => numTurns(tail, currentDirection)
      case Seq(head, tail @ _*) => numTurns(tail, head) + 1
    }
  }
}
