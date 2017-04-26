package com.coding42.engine

import org.lwjgl.opengl.GL11

/**
  * Trait for common components
  */
sealed trait Component {
  def ref: ComponentRef
  def gameObjectRef: GameObjectRef
  def gameObject(world: World): GameObject = world.gameObjects(gameObjectRef) // TODO this might blow up when deleting GOs
}

case class SpriteRenderer(ref: ComponentRef, gameObjectRef: GameObjectRef, texture: Texture) extends Component {

  def draw(world: World): Unit = {

    val position = gameObject(world).transform.position
    val scale = gameObject(world).transform.scale

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id)

    // store the current model matrix
    GL11.glPushMatrix()
    // bind to the appropriate texture for this sprite
    //    texture.bind
    // translate to the right location and prepare to draw
    GL11.glTranslatef(position.x, position.z, 0)
//    GL11.glColor3f(1, 1, 1)
    // draw a quad textured to match the sprite
    GL11.glBegin(GL11.GL_QUADS)

    GL11.glTexCoord2f(0, 0)
    GL11.glVertex2f(0, 0)

    GL11.glTexCoord2f(0, 1)
    GL11.glVertex2f(0, scale.z)

    GL11.glTexCoord2f(1, 1)
    GL11.glVertex2f(scale.x, scale.z)

    GL11.glTexCoord2f(1, 0)
    GL11.glVertex2f(scale.x, 0)

    GL11.glEnd()
    // restore the model view matrix to prevent contamination
    GL11.glPopMatrix()
  }

}

object SpriteRenderer {

  def apply(gameObjectRef: GameObjectRef, texture: Texture): SpriteRenderer = {
    new SpriteRenderer(ComponentRef(), gameObjectRef, texture)
  }

}

trait CodeLogic extends Component {
  def handleKeyDown(key: Int)(world: World): World = world
  def handleKeyUp(key: Int)(world: World): World = world
//  def handleKeyPressed(key: Int)(world: World): World = world

  def onStart(world: World): World = world
  def onUpdate(deltaTime: Float)(world: World): World = world

  def onCollisionEnter(collision: Collision)(world: World): World = world
}

sealed trait Collider extends Component {

  def relativePosition: Position

  def trigger: Boolean

  def position(world: World): Position = gameObject(world).transform.position + relativePosition
}

object Collider {

  def hasCollision(a: Collider, b: Collider)(world: World): Boolean = {
    (a,b) match {
      case (b1:SphereCollider, b2:SphereCollider) => hasCollision(b1, b2)(world: World)
    }
  }

  def hasCollision(a: SphereCollider, b: SphereCollider)(world: World): Boolean = {
    Position.distance(a.position(world), b.position(world)) < a.radius + b.radius
  }

  def collisions(c: Iterable[Collider])(world: World): Map[GameObjectRef, Set[Collision]] = {
    import cats.Semigroup
    import cats.implicits._
    val semigroup = Semigroup[Map[GameObjectRef, Set[Collision]]] // TODO check this works fine, maybe a little test ;)
    c match {
      case Nil => Map.empty
      case h :: t => semigroup.combine(collisions(h, t)(world), collisions(t)(world))
    }
  }

  private def collisions(c: Collider, iterable: Iterable[Collider])(world: World): Map[GameObjectRef, Set[Collision]] = {
    iterable.collect {
      case o if hasCollision(c, o)(world) =>
        Map(c.gameObjectRef -> Set(Collision(o)), o.gameObjectRef -> Set(Collision(c)))
    }.fold(Map.empty)(_ ++ _)
  }

}

case class SphereCollider(ref: ComponentRef, gameObjectRef: GameObjectRef, relativePosition: Position, radius: Float, trigger: Boolean) extends Collider

object SphereCollider {

  def apply(gameObjectRef: GameObjectRef, relativePosition: Position, radius: Float, trigger: Boolean): SphereCollider = {
    new SphereCollider(ComponentRef(), gameObjectRef, relativePosition, radius, trigger)
  }
  
}