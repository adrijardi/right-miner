package com.coding42.engine

import org.lwjgl.opengl.GL11
import GL11._

trait Drawable[T <: Component] {
  def draw(t: T)(world: World): Unit
}

object Drawable {

  def draw(t: Component)(world: World): Unit = t match {
    case c: SpriteRenderer  => SpriteDrawable.draw(c)(world)
    case c: SphereCollider  => SphereColliderDrawable.draw(c)(world)
    case c: BoxCollider     => BoxColliderDrawable.draw(c)(world)
    case _: CodeLogic       =>
  }

  object SpriteDrawable extends Drawable[SpriteRenderer] {

    override def draw(t: SpriteRenderer)(world: World): Unit = {

      val position = t.gameObject(world).transform.position
      val scale = t.gameObject(world).transform.scale

      glBindTexture(GL_TEXTURE_2D, t.texture.id)

      // store the current model matrix
      glPushMatrix()
      // bind to the appropriate texture for this sprite
      //    texture.bind
      // translate to the right location and prepare to draw
      glTranslatef(position.x, position.z, 0)
      //    glColor3f(1, 1, 1)
      // draw a quad textured to match the sprite
      glBegin(GL_QUADS)

      glTexCoord2f(0, 0)
      glVertex2f(0, 0)

      glTexCoord2f(0, 1)
      glVertex2f(0, scale.z)

      glTexCoord2f(1, 1)
      glVertex2f(scale.x, scale.z)

      glTexCoord2f(1, 0)
      glVertex2f(scale.x, 0)

      glEnd()
      // restore the model view matrix to prevent contamination
      glPopMatrix()
    }
  }

  object BoxColliderDrawable extends Drawable[BoxCollider] {

    override def draw(t: BoxCollider)(world: World): Unit =
      if(world.gameConfig.debug) {
        val position = t.gameObject(world).transform.position + t.relativePosition

        glLineWidth(4f)

        glBegin(GL_LINE_LOOP)
        glVertex3f(position.x, position.z, 0f)
        glVertex3f(position.x + t.size.x, position.z, 0f)
        glVertex3f(position.x + t.size.x, position.z + t.size.z, 0f)
        glVertex3f(position.x, position.z + t.size.z, 0f)
        glEnd()
      }
  }

  object SphereColliderDrawable extends Drawable[SphereCollider] {

    override def draw(t: SphereCollider)(world: World): Unit =
      if(world.gameConfig.debug) {
        val position = t.gameObject(world).transform.position

        glLineWidth(4f)

        glBegin(GL_LINE_LOOP)
        glVertex3f(position.x - t.radius, position.z, 0f)
        glVertex3f(position.x, position.z - t.radius, 0f)
        glVertex3f(position.x + t.radius, position.z, 0f)
        glVertex3f(position.x, position.z + t.radius, 0f)
        glEnd()
      }
  }

}
