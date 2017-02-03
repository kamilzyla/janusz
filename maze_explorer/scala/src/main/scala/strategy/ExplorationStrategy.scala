package strategy

import maze.{Maze, MazeField}
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
  * Created by zak on 10/28/16.
  */
class ExplorationStrategy(getNextField: (ExplorationState, MazeField) => MazeField) {
  val logger = LoggerFactory.getLogger(this.getClass)

  def explore(maze: Maze): Seq[MazeField] = {
    logger.info(s"Explore: ${maze.name}")

    val state: ExplorationState = new ExplorationState(maze)
    val path: mutable.Queue[MazeField] = mutable.Queue.empty
    var currentField = maze.startField

    while (shouldExplore(state)) {
      logger.debug(s"CurrentField: $currentField")
      state.update(currentField)
      val nextField = getNextField(state, currentField)
      assert(maze.getNeighbours(currentField) contains nextField)
      path.enqueue(nextField)
      currentField = nextField
    }

    path
  }

  def shouldExplore(state: ExplorationState): Boolean = {
    val bestPossibleCosts = new BellmanFord(
      transitionCostWhenNoWall = 1d,
      transitionsWhenUnknownWall = Allowed(cost = 1d)
    ).run(state)
    val bestKnownCosts = new BellmanFord(
      transitionCostWhenNoWall = 1d,
      transitionsWhenUnknownWall = Forbidden
    ).run(state)

    val pathToCentralFieldIsUnknown = !(bestKnownCosts.keySet contains state.startField)
    pathToCentralFieldIsUnknown || (bestPossibleCosts(state.startField).totalCost < bestKnownCosts(state.startField).totalCost)
  }
}
