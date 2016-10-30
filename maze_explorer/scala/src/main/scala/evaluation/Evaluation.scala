package evaluation

import maze.MazeField

/**
  * Created by zak on 10/30/16.
  */
trait Evaluation {
  def evaluate(path: Seq[MazeField]): Double
}
