package com.coding42.engine

import org.lwjgl.opengl.GL11

/**
  * Trait for common components
  */
sealed trait Component {
  def gameObjectRef: GameObjectRef
  def gameObject(world: World): GameObject = world.gameObjects(gameObjectRef) // TODO this might blow up when deleting GOs
}

case class SpriteRenderer(gameObjectRef: GameObjectRef, texture: Texture) extends Component {

  def draw(world: World): Unit = {

    val position = gameObject(world).transform.position

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id)

    // store the current model matrix
    GL11.glPushMatrix()
    // bind to the appropriate texture for this sprite
    //    texture.bind
    // translate to the right location and prepare to draw
    GL11.glTranslatef(position.x, position.z, 0)
    GL11.glColor3f(1, 1, 1)
    // draw a quad textured to match the sprite
    GL11.glBegin(GL11.GL_QUADS)
    GL11.glTexCoord2f(0, 0)
    GL11.glVertex2f(0, 0)
    GL11.glTexCoord2f(0, texture.height)
    GL11.glVertex2f(0, world.gameConfig.screenHeight)
    GL11.glTexCoord2f(texture.width, texture.height)
    GL11.glVertex2f(world.gameConfig.screenWidth, world.gameConfig.screenHeight)
    GL11.glTexCoord2f(texture.width, 0)
    GL11.glVertex2f(world.gameConfig.screenWidth, 0)

    GL11.glEnd()
    // restore the model view matrix to prevent contamination
    GL11.glPopMatrix()
  }

}

trait CodeLogic extends Component {
  def handleKeyDown(key: Int)(world: World): World = world
  def handleKeyUp(key: Int)(world: World): World = world
  def handleKeyPressed(key: Int)(world: World): World = world
}
