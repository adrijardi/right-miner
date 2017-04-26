package com.coding42.engine

/**
  * Object that contains all elements of the world. A new instance will be created when any change to the world is made
  */
case class World(gameObjects: Map[GameObjectRef, GameObject], components: Map[GameObjectRef, Set[Component]], gameConfig: GameConfig) {

  def withGameObject(gameObject: GameObject): World = {
    copy(gameObjects = gameObjects.updated(gameObject.ref, gameObject))
  }

  def withComponent(component: Component): World = {
    val prevGoComponents = components.getOrElse(component.gameObjectRef, Set.empty)
    val newComponents = components + (component.gameObjectRef -> (prevGoComponents + component))
    copy(components = newComponents)
  }

  def allComponents: Iterable[Component] = components.values.flatten

}

object World {

  def empty(gameConfig: GameConfig): World = new World(Map.empty, Map.empty, gameConfig)

}
