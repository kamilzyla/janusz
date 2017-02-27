package strategy

import maze.{Maze, MazeField}
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
  * Created by zak on 10/28/16.
  */
class ExplorationStrategy(nextFieldProvider: NextFieldProvider) {
  val logger = LoggerFactory.getLogger(this.getClass)

  def explore(maze: Maze): Seq[MazeField] = {
    logger.info(s"Explore: ${maze.name}")

    val state: ExplorationState = new ExplorationState(maze)
    val path: mutable.Queue[MazeField] = mutable.Queue.empty
    var currentField = maze.startField

    while (shouldExplore(state)) {
      logger.debug(s"CurrentField: $currentField")
      state.update(currentField)
      val nextField = nextFieldProvider.getNextField(state, currentField)
      assert(
        maze.getNeighbours(currentField) contains nextField,
        s"Invalid move from: $currentField to: $nextField in maze: ${maze.name}"
      )
      path.enqueue(nextField)
      currentField = nextField
    }

    path
  }

  def shouldExplore(state: ExplorationState): Boolean =
    !BellmanFord.isBestPathFromFieldKnown(state, state.startField)
}
