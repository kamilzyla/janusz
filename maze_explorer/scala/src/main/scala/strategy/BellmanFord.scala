package strategy

import maze.{Direction, Maze, MazeField, MazeWall}

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
      val path: mutable.Queue[MazeField] = mutable.Queue.empty
      var currentField = maze.startField

      while (shouldExplore(state)) {
        state.update(currentField)
        currentField = getNextFiled(currentField, state)
        path.enqueue(currentField)
      }

      path
    }

    private def getNextFiled(currentField: MazeField, state: State): MazeField = {
      val estimatedCosts = runBellmanFord(maze, state, currentField)
      getBestNeighbour(maze, currentField, estimatedCosts)
    }

    private def shouldExplore(state: State): Boolean = (state.getVisitedFields intersect maze.centerFields).isEmpty
  }
}

class State(maze: Maze) {
  private val seenWalls: mutable.Set[MazeWall] = mutable.Set.empty
  private val seenGaps: mutable.Set[MazeWall] = mutable.Set.empty
  private val visitedFields: mutable.Set[MazeField] = mutable.Set.empty

  def update(field: MazeField): Unit = {
    val allPossibleWalls = Direction.getAll.map(MazeWall(field, _))
    val newWalls = maze.getWalls(field)
    val newGaps = allPossibleWalls diff newWalls

    seenWalls ++= newWalls
    seenGaps ++= newGaps
    visitedFields += field
  }

  def getVisitedFields = visitedFields
}
