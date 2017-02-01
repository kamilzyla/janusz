package strategy

import maze.{Direction, Maze, MazeField}

import scala.collection.mutable

/**
  * Created by zak on 1/17/17.
  */
class BellmanFord(getCostsForNeighbours: (ExplorationState, MazeField) => Set[(MazeField, Double)]) {
  def getNextFiled(currentField: MazeField, state: ExplorationState): MazeField = {
    val estimatedCosts = runBellmanFord(state)
    assert(estimatedCosts contains currentField)
    val Cost(nextField, _) = estimatedCosts(currentField)
    nextField
  }

  private def runBellmanFord(state: ExplorationState): Map[MazeField, Cost] = {
    val queue: mutable.PriorityQueue[FieldWithCost] = mutable.PriorityQueue.empty[FieldWithCost]
    val costs: mutable.Map[MazeField, Cost] = mutable.Map.empty

    def addInitialFields(): Unit =
      queue.enqueue(state.centralFields.map(field => FieldWithCost(field, Cost(field, 0d))).toSeq: _*)

    addInitialFields()
    while (queue.nonEmpty) {
      val FieldWithCost(currentField, cost) = queue.dequeue()
      if (!(costs.keySet contains currentField)) {
        costs.put(currentField, cost)
        val transitionCosts = getCostsForNeighbours(state, currentField)
        val newCosts = transitionCosts map {
          case (neighbour, transitionCost) => FieldWithCost(neighbour, Cost(currentField, cost.totalCost + transitionCost))
        }
        queue.enqueue(newCosts.toSeq: _*)
      }
    }
    costs.toMap
  }

  case class Cost(prevField: MazeField, totalCost: Double)

  case class FieldWithCost(field: MazeField, cost: Cost)

  implicit val FieldWithCostOrd = new Ordering[FieldWithCost] {
    override def compare(x: FieldWithCost, y: FieldWithCost): Int = y.cost.totalCost compare x.cost.totalCost
  }
}

object ReachedCentralField {
  def shouldExplore(state: ExplorationState): Boolean =
    (state.getVisitedFields intersect state.centralFields).isEmpty
}

class NeighboursCostComputer(
    transitionCostWhenNoWall: Double,
    transitionsWhenUnknownWall: TransitionsWhenUnknownWall
) {
  def getCostsForNeighbours(state: ExplorationState, currentField: MazeField): Set[(MazeField, Double)] = {
    val allPossibleNeighbours = Direction.getAll.map(Maze.getNeighbour(currentField, _))
    allPossibleNeighbours.map(neighbour => state.getWallKnowledge(currentField, neighbour) match {
      case Present => None
      case Absent => Some((neighbour, transitionCostWhenNoWall))
      case Unknown => transitionsWhenUnknownWall match {
        case Allowed(cost) => Some(neighbour, cost)
        case Forbidden => None
      }
    }).filter(_.isDefined).map(_.get)
  }
}

sealed abstract class TransitionsWhenUnknownWall

case class Allowed(cost: Double) extends TransitionsWhenUnknownWall

case object Forbidden extends TransitionsWhenUnknownWall
