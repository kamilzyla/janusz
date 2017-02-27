package strategy

import maze.{Direction, Maze, MazeField}
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
  * Created by zak on 1/17/17.
  */

trait NextFieldProvider {
  def getNextField(state: ExplorationState, currentField: MazeField): MazeField
}

class BellmanFord(
  transitionCostWhenNoWall: Double,
  transitionsWhenUnknownWall: TransitionsWhenUnknownWall
) extends NextFieldProvider {
  val logger = LoggerFactory.getLogger(this.getClass)

  val fieldsToStart: mutable.Stack[MazeField] = mutable.Stack.empty[MazeField]
  val visitedFields: mutable.Set[MazeField] = mutable.Set.empty[MazeField]

  override def getNextField(state: ExplorationState, currentField: MazeField): MazeField = {
    visitedFields.add(currentField)
    if (shouldStepBack(state, currentField)) {
      fieldsToStart.pop()
      fieldsToStart.top
    } else {
      val estimatedCosts = run(state)
//      assert(estimatedCosts contains currentField)
      // Nie przechodź do najlepszego tylko do najlepszego nie odwiedzonego
      // TODO: Zastanowić się dlaczego się zapętla
      val nextField = state.getNeighbours(currentField).diff(visitedFields)
        .map(field => field -> estimatedCosts(field))
        .maxBy({ case (_, Cost(_, totalCost)) => totalCost })
        ._1
      fieldsToStart.push(nextField)
      //      logger.debug("Push")
      nextField
    }
  }

  private def shouldStepBack(state: ExplorationState, field: MazeField): Boolean =
    allNeighboursVisited(state, field) || BellmanFord.isBestPathFromFieldKnown(state, field)

  // Not sure if needed
  private def allNeighboursVisited(state: ExplorationState, field: MazeField): Boolean =
  state.getNeighbours(field)
    .forall(neighbour => state.getVisitedFields contains neighbour)

  private def run(state: ExplorationState): Map[MazeField, Cost] = {
    val queue: mutable.PriorityQueue[FieldWithCost] = mutable.PriorityQueue.empty[FieldWithCost]
    val costs: mutable.Map[MazeField, Cost] = mutable.Map.empty

    def addInitialFields(): Unit =
      queue.enqueue(state.centralFields.map(field => FieldWithCost(field, Cost(field, 0d))).toSeq: _*)

    addInitialFields()
    while (queue.nonEmpty) {
      val FieldWithCost(currentField, cost) = queue.dequeue()
      if (!(costs.keySet contains currentField)) {
        costs.put(currentField, cost)
        //        logger.debug(s"BellmanFord $currentField -> $cost")
        val transitionCosts = getTransitionCosts(state, currentField)
        val newCosts = transitionCosts
          .filterNot({ case (neighbour, _) => costs.keySet contains neighbour })
          .map({
            case (neighbour, transitionCost) => FieldWithCost(neighbour, Cost(currentField, cost.totalCost + transitionCost))
          })
        queue.enqueue(newCosts.toSeq: _*)
      }
    }
    costs.toMap
  }

  private def getTransitionCosts(state: ExplorationState, currentField: MazeField): Set[(MazeField, Double)] = {
    val allPossibleNeighbours = Direction.getAll.map(Maze.getPossibleNeighbour(currentField, _)).filter(state.isInside)
    allPossibleNeighbours.map(neighbour => state.getWallKnowledge(currentField, neighbour) match {
      case Present => None
      case Absent => Some((neighbour, transitionCostWhenNoWall))
      case Unknown => transitionsWhenUnknownWall match {
        case Allowed(cost) => Some((neighbour, cost))
        case Forbidden => None
      }
    }).filter(_.isDefined).map(_.get)
  }

  case class Cost(prevField: MazeField, totalCost: Double)

  case class FieldWithCost(field: MazeField, cost: Cost)

  implicit val FieldWithCostOrd = new Ordering[FieldWithCost] {
    override def compare(x: FieldWithCost, y: FieldWithCost): Int = y.cost.totalCost compare x.cost.totalCost
  }
}

object BellmanFord {
  def isBestPathFromFieldKnown(state: ExplorationState, field: MazeField): Boolean = {
    // Set tych pól tylko rośnie
    val bestPossibleCosts = new BellmanFord(
      transitionCostWhenNoWall = 1d,
      transitionsWhenUnknownWall = Allowed(cost = 1d)
    ).run(state)
    val bestKnownCosts = new BellmanFord(
      transitionCostWhenNoWall = 1d,
      transitionsWhenUnknownWall = Forbidden
    ).run(state)

    val pathToCentralFieldIsKnown = bestKnownCosts.keySet contains field
    pathToCentralFieldIsKnown && (bestPossibleCosts(field).totalCost == bestKnownCosts(field).totalCost)
  }
}


sealed abstract class TransitionsWhenUnknownWall

case class Allowed(cost: Double) extends TransitionsWhenUnknownWall

case object Forbidden extends TransitionsWhenUnknownWall
