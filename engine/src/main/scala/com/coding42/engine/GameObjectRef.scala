package com.coding42.engine

/**
  * Reference to a game object that doesn't change between instances
  */
case class GameObjectRef(ref: Int)

object GameObjectRef {

  // TODO force calling this
  def apply(): GameObjectRef = ElementRef(GameObjectRef(_))
}
