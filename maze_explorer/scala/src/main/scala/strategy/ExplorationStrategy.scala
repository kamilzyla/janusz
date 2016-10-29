package strategy

import maze.{Maze, MazeField}

/**
  * Created by zak on 10/28/16.
  */
trait ExplorationStrategy {
  def explore(maze: Maze): Seq[MazeField]
}
