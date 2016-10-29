package strategy

import org.scalatest.{FlatSpec, Matchers}
import test_utils.SampleMaze

/**
  * Created by zak on 10/29/16.
  */
class DfsTest extends FlatSpec with Matchers {
  behavior of "Dfs"

  val strategy = new Dfs()

  it should "start and end in field (0, 0)" in {
    fail()
  }

  it should "visit each maze field exactly once" in {
    val path = strategy.explore(SampleMaze.maze)
    path foreach {f => println(s"(${f.x}, ${f.y})")}
  }

  it should "construct a path of neighbouring fields" in {
    fail()
  }
}
