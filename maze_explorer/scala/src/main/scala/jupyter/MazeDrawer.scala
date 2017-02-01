package jupyter

import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage

import maze._

/**
  * Created by zak on 11/13/16.
  */

object MazeDrawer {
  val scale = 32

  def draw(maze: Maze): BufferedImage = {
    val img = new BufferedImage(maze.numColumns * scale + 1, maze.numRows * scale + 1, BufferedImage.TYPE_INT_ARGB)
    val graphics = img.createGraphics()

    graphics.setPaint(Color.BLACK)
    maze.fields.flatten foreach drawField(graphics)

    img
  }

  private def drawField(graphics: Graphics)(field: MazeField): Unit = {
    field.walls foreach drawWall(graphics, field)
  }

  private def drawWall(graphics: Graphics, field: MazeField)(wall: MazeWall) = {
    val (startCorner, endCorner) = Corner.getWallCorners(wall)
    val (x1, y1) = getPoint(field, startCorner)
    val (x2, y2) = getPoint(field, endCorner)
    graphics.drawLine(x1, y1, x2, y2)
  }

  private def getPoint(field: MazeField, corner: Corner): (Int, Int) =
    (scale * (field.x + corner.x), scale * (field.y + corner.y))
}
