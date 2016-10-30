package strategy

import maze.MazeField
import org.scalatest.{FlatSpec, Matchers}
import test_utils.SampleMaze

/**
  * Created by zak on 10/29/16.
  */
class DfsTest extends FlatSpec with Matchers {
  behavior of "Dfs"

  private val strategy = new Dfs()
  private val path = strategy.explore(SampleMaze.maze)

  private def areNeighbours(a: MazeField, b: MazeField): Boolean =
    SampleMaze.maze.getNeighbours(a) contains b

  it should "start and end in field (0, 0)" in {
    path.head shouldBe SampleMaze.maze.startField
    path.last shouldBe SampleMaze.maze.startField
  }

  it should "visit each maze field" in {
    path.toSet shouldBe SampleMaze.maze.fields.flatten.toSet
  }

  it should "construct a path of neighbouring fields" in {
    (0 until path.length - 1) foreach { idx =>
      assert(areNeighbours(path(idx), path(idx + 1)))
    }
  }
}
