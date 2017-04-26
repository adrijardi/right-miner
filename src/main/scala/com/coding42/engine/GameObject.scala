package com.coding42.engine

/**
  * class wrapping the data for an element on the game world
  */
case class GameObject(ref: GameObjectRef, name: String, transform: Transform) {
  def withPos(position: Position): GameObject = copy(transform = transform.copy(position = position))
}

object GameObject {

  def apply(name: String, transform: Transform): GameObject = {
    new GameObject(GameObjectRef(), name, transform)
  }

}