package strategy

import maze.{Direction, Maze, MazeField}
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
  * Created by zak on 1/17/17.
  */
class BellmanFord(
    transitionCostWhenNoWall: Double,
    transitionsWhenUnknownWall: TransitionsWhenUnknownWall
) {
  val logger = LoggerFactory.getLogger(this.getClass)

  def getNextFiled(state: ExplorationState, currentField: MazeField): MazeField = {
    val estimatedCosts = run(state)
    assert(estimatedCosts contains currentField)
    val Cost(nextField, _) = estimatedCosts(currentField)
    nextField
  }

  def run(state: ExplorationState): Map[MazeField, Cost] = {
    val queue: mutable.PriorityQueue[FieldWithCost] = mutable.PriorityQueue.empty[FieldWithCost]
    val costs: mutable.Map[MazeField, Cost] = mutable.Map.empty

    def addInitialFields(): Unit =
      queue.enqueue(state.centralFields.map(field => FieldWithCost(field, Cost(field, 0d))).toSeq: _*)

    addInitialFields()
    while (queue.nonEmpty) {
      val FieldWithCost(currentField, cost) = queue.dequeue()
      if (!(costs.keySet contains currentField)) {
        costs.put(currentField, cost)
        logger.debug(s"BellmanFord $currentField -> $cost")
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

  def getTransitionCosts(state: ExplorationState, currentField: MazeField): Set[(MazeField, Double)] = {
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

sealed abstract class TransitionsWhenUnknownWall

case class Allowed(cost: Double) extends TransitionsWhenUnknownWall

case object Forbidden extends TransitionsWhenUnknownWall
