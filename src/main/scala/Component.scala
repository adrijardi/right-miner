import org.lwjgl.opengl.GL11
import Boot.{height, width}

/**
  * Trait for common components
  */
sealed trait Component {
  lazy val gameObject: GameObject = Component.components(this)
}

object Component {
  var components: Map[Component, GameObject] = Map.empty
}

case class SpriteRenderer(texture: Texture) extends Component {

  def draw(): Unit = {

    val position = gameObject.transform.position

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id)

    // store the current model matrix
    GL11.glPushMatrix()
    // bind to the appropriate texture for this sprite
    //    texture.bind
    // translate to the right location and prepare to draw
    GL11.glTranslatef(position.x, position.y, 0)
    GL11.glColor3f(1, 1, 1)
    // draw a quad textured to match the sprite
    GL11.glBegin(GL11.GL_QUADS)
    GL11.glTexCoord2f(0, 0)
    GL11.glVertex2f(0, 0)
    GL11.glTexCoord2f(0, texture.height)
    GL11.glVertex2f(0, height)
    GL11.glTexCoord2f(texture.width, texture.height)
    GL11.glVertex2f(width, height)
    GL11.glTexCoord2f(texture.width, 0)
    GL11.glVertex2f(width, 0)

    GL11.glEnd()
    // restore the model view matrix to prevent contamination
    GL11.glPopMatrix()
  }

}
