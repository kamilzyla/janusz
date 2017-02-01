package strategy

import maze.{Maze, MazeField}

import scala.collection.mutable

/**
  * Created by zak on 10/28/16.
  */
class ExplorationStrategy(shouldExplore: (ExplorationState => Boolean), getNextField: (ExplorationState, MazeField) => MazeField) {
  def explore(maze: Maze): Seq[MazeField] = {
    val state: ExplorationState = new ExplorationState(maze)
    val path: mutable.Queue[MazeField] = mutable.Queue.empty
    var currentField = maze.startField

    while (shouldExplore(state)) {
      state.update(currentField)
      val nextField = getNextField(state, currentField)
      assert(maze.getNeighbours(currentField) contains nextField)
      path.enqueue(nextField)
      currentField = nextField
    }

    path
  }
}
