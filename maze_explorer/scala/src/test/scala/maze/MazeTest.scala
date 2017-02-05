package maze

import org.scalatest.{FlatSpec, Matchers}
import test_utils.SampleMaze

/**
  * Created by zak on 2/5/17.
  */
class MazeTest extends FlatSpec with Matchers {
  behavior of "Maze"

  it should "return field (0, 0) as a start field" in {
    SampleMaze.maze.startField shouldBe MazeField(0, 0)
  }

  it should "return singleton MazeField(2, 2) as a central field of maze 5 x 5" in {
    SampleMaze.maze.centralFields shouldBe Set(MazeField(2, 2))
  }

  it should "return four central fields as a central fields of maze 6 x 6" in {
    Maze("testMaze", numRows = 6, numColumns = 6, walls = Set()).centralFields shouldBe Set(
      MazeField(2, 2),
      MazeField(2, 3),
      MazeField(3, 2),
      MazeField(3, 3)
    )
  }

  it should "return proper neighbours of a filed" in {
    SampleMaze.maze.getNeighbours(MazeField(0, 0)) shouldBe Set(MazeField(0, 1), MazeField(1, 0))
    SampleMaze.maze.getNeighbours(MazeField(2, 2)) shouldBe Set(MazeField(2, 1))
    SampleMaze.maze.getNeighbours(MazeField(1, 3)) shouldBe Set(MazeField(0, 3), MazeField(2, 3))
  }

  it should "return walls of a field" in {
    SampleMaze.maze.getWalls(MazeField(0, 0)) shouldBe Set(
      MazeWall(MazeField(0, 0), South),
      MazeWall(MazeField(0, 0), West)
    )
    SampleMaze.maze.getWalls(MazeField(2, 2)) shouldBe Set(
      MazeWall(MazeField(2, 2), West),
      MazeWall(MazeField(2, 2), North),
      MazeWall(MazeField(2, 2), East)
    )
    SampleMaze.maze.getWalls(MazeField(1, 3)) shouldBe Set(
      MazeWall(MazeField(1, 3), North),
      MazeWall(MazeField(1, 3), South)
    )
  }

  it should "classify fields outside" in {
    SampleMaze.maze.isInside(MazeField(0, -1)) shouldBe false
    SampleMaze.maze.isInside(MazeField(5, 0)) shouldBe false
    SampleMaze.maze.isInside(MazeField(3, 5)) shouldBe false
    SampleMaze.maze.isInside(MazeField(-1, 2)) shouldBe false
  }

  it should "classify fields inside" in {
    SampleMaze.maze.isInside(MazeField(0, 0)) shouldBe true
    SampleMaze.maze.isInside(MazeField(0, 4)) shouldBe true
    SampleMaze.maze.isInside(MazeField(4, 0)) shouldBe true
    SampleMaze.maze.isInside(MazeField(4, 4)) shouldBe true
  }

  it should "return possible neighbour in all directions" in {
    Maze.getPossibleNeighbour(MazeField(2, 2), North) shouldBe MazeField(2, 3)
    Maze.getPossibleNeighbour(MazeField(2, 2), West) shouldBe MazeField(1, 2)
    Maze.getPossibleNeighbour(MazeField(2, 2), South) shouldBe MazeField(2, 1)
    Maze.getPossibleNeighbour(MazeField(2, 2), East) shouldBe MazeField(3, 2)
  }
}
