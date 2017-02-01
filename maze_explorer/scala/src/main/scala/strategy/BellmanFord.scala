package strategy

import maze.{Maze, MazeField, MazeWall}

import scala.collection.mutable

/**
  * Created by zak on 1/17/17.
  */
object BellmanFord extends ExplorationStrategy {
  def explore(maze: Maze): Seq[MazeField] = {
    new BellmanFordRunner(maze).run()
  }

  private class BellmanFordRunner(maze: Maze) {

    def run(): Seq[MazeField] = {
      val state: State = new State(maze)
      val fields: mutable.Queue[MazeField] = mutable.Queue.empty
      var currentField = maze.startField

      while (shouldExplore()) {
        state.update(currentField)
        currentField = getNextFiled(currentField, state)
        fields.enqueue(currentField)
      }

      fields
    }

    def getNextFiled(currentField: MazeField, state: State): MazeField = {
      val estimatedCosts = runBellmanFord(maze, state, currentField)
      getBestNeighbour(maze, currentField, estimatedCosts)
    }
  }
}

class State(maze: Maze) {
  var seenWalls: Set[MazeWall]

  def update(field: MazeField): Unit
}

object State {
  val unknown: State = State(Map.empty)
}