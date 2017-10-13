package com.coding42.engine

import com.coding42.engine.collision.CollisionChecker

/**
  * Trait for common components
  */
sealed trait Component {
  def ref: ComponentRef
  def gameObjectRef: GameObjectRef
  def gameObject(world: World): GameObject = world.gameObjects(gameObjectRef) // TODO this might blow up when deleting GOs
}

case class SpriteRenderer(ref: ComponentRef, gameObjectRef: GameObjectRef, texture: Texture) extends Component

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
      case o if CollisionChecker.hasCollision(c, o)(world) =>
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

case class BoxCollider(ref: ComponentRef, gameObjectRef: GameObjectRef, relativePosition: Position, size: Position, trigger: Boolean) extends Collider

object BoxCollider {

  def apply(gameObjectRef: GameObjectRef, relativePosition: Position, size: Position, trigger: Boolean): BoxCollider = {
    new BoxCollider(ComponentRef(), gameObjectRef, relativePosition, size, trigger)
  }
}
