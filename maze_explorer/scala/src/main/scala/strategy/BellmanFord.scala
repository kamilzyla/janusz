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
    val costWhenNoWall = 1d
    val costWhenUnknownWall = 1.5d

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
      val estimatedCosts = runBellmanFord(state, currentField)
      assert(estimatedCosts contains currentField)
      val Cost(nextField, _) = estimatedCosts(currentField)
      nextField
    }

    private def shouldExplore(state: State): Boolean =
      (state.getVisitedFields intersect maze.centralFields).isEmpty

    private def runBellmanFord(state: State, field: MazeField): Map[MazeField, Cost] = {
      val queue: mutable.PriorityQueue[(Cost, MazeField)] = mutable.PriorityQueue.empty[(Cost, MazeField)]
      val costs: mutable.Map[MazeField, Cost] = mutable.Map.empty

      def addInitialFields(): Unit =
        queue.enqueue(maze.centralFields.map(field => (Cost(field, 0d), field)).toSeq: _*)

      addInitialFields()
      while (queue.nonEmpty) {
        val (cost, currentField) = queue.dequeue()
        if (!(costs.keySet contains currentField)) {
          costs.put(currentField, cost)
          val newCosts = getCostsForNeighbours(state, currentField, cost)
          queue.enqueue(newCosts.toSeq: _*)
        }
      }
      costs.toMap
    }

    private def getCostsForNeighbours(state: State, currentField: MazeField, cost: Cost): Set[(Cost, MazeField)] = {
      val allPossibleNeighbours = Direction.getAll.map(Maze.getNeighbour(currentField, _))
      allPossibleNeighbours
          .filterNot(neighbour => state.isWallBetween(currentField, neighbour))
          .map(neighbour => {
            val transitionCost = if (state.isGapBetween(currentField, neighbour)) {
              costWhenNoWall
            } else {
              costWhenUnknownWall
            }
            (Cost(currentField, cost.totalCost + transitionCost), neighbour)
          })
    }
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

  def isWallBetween(firstField: MazeField, secondFiled: MazeField): Boolean =
    seenWalls contains MazeWall(firstField, secondFiled)

  def isGapBetween(firstField: MazeField, secondFiled: MazeField): Boolean =
    seenGaps contains MazeWall(firstField, secondFiled)

  def getVisitedFields = visitedFields
}

case class Cost(prevField: MazeField, totalCost: Double) extends Ordering[Cost] {
  override def compare(x: Cost, y: Cost): Int = x.totalCost compare y.totalCost
}
