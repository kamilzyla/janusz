package strategy

import maze.{Direction, Maze, MazeField, MazeWall}

import scala.collection.mutable

/**
  * Created by zak on 2/1/17.
  */
class ExplorationState(maze: Maze) {
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

  def getWallKnowledge(firstField: MazeField, secondField:MazeField): WallKnowledge = {
    val wall = MazeWall(firstField, secondField)
    (seenWalls contains wall, seenGaps contains wall) match {
      case (true, false) => Present
      case (false, true) => Absent
      case (false, false) => Unknown
      case _ => assert(false, "Inconsistent knowledge about walls"); ???
    }
  }

  def getVisitedFields = visitedFields

  def centralFields = maze.centralFields
}

abstract sealed class WallKnowledge

case object Present extends WallKnowledge

case object Absent extends WallKnowledge

case object Unknown extends WallKnowledge