package strategy

import maze.{Maze, MazeField}

/**
  * Created by zak on 10/28/16.
  */
class Dfs extends ExplorationStrategy {
  override def explore(maze: Maze): Seq[MazeField] = {
    dfs(maze, Set.empty, maze.startField)
  }

  private def dfs(maze: Maze, visited: Set[MazeField], currentField: MazeField): Seq[MazeField] = {
    val neighbours = maze.getNeighbours(currentField)
    val neighboursPaths = visitNeighbours(maze, visited + currentField, neighbours).filterNot(_.isEmpty)
    if (neighboursPaths.isEmpty) {
      Seq(currentField)
    } else {
      val connectedNeighboursPaths = neighboursPaths.reduce((s1, s2) => s1 ++ (currentField +: s2))
      currentField +: connectedNeighboursPaths :+ currentField
    }

  }

  private def visitNeighbours(
      maze: Maze,
      visited: Set[MazeField],
      neighbours: Set[MazeField]
  ): Seq[Seq[MazeField]] = {
    if (neighbours.isEmpty) {
      Seq()
    } else {
      if (visited contains neighbours.head) {
        visitNeighbours(maze, visited, neighbours.tail)
      } else {
        val newVisited = dfs(maze, visited + neighbours.head, neighbours.head)
        newVisited +: visitNeighbours(maze, visited ++ newVisited.toSet, neighbours.tail)
      }
    }
  }

}
