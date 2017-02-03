package maze

import org.scalatest.{FlatSpec, Matchers}
import test_utils.SampleMaze

class MazeReaderTest extends FlatSpec with Matchers {
  behavior of "MazeReader"

  it should "Read maze walls from fields byte format" in {
    val mazeReader = new MazeReader(numRows = 5, numColumns = 5)
    val walls = mazeReader.readMazeWalls(SampleMaze.fieldsBytes)

    walls.size shouldEqual 35

    assert(walls contains MazeWall(MazeField(0, 0), South))
    assert(walls contains MazeWall(MazeField(0, 0), West))
    assert(!(walls contains MazeWall(MazeField(0, 0), North)))
    assert(!(walls contains MazeWall(MazeField(0, 0), East)))

    assert(walls contains MazeWall(MazeField(0, 4), North))
    assert(walls contains MazeWall(MazeField(0, 4), West))
    assert(!(walls contains MazeWall(MazeField(0, 4), South)))
    assert(!(walls contains MazeWall(MazeField(0, 4), East)))
  }
}
