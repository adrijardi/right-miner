package com.coding42.engine

/**
  * Object that contains all elements of the world. A new instance will be created when any change to the world is made
  */
case class World(gameObjects: Map[GameObjectRef, GameObject], components: Map[ComponentRef, Component], gameConfig: GameConfig) {

  def withGameObject(gameObject: GameObject): World = {
    copy(gameObjects = gameObjects.updated(gameObject.ref, gameObject))
  }

  def withComponent(component: Component): World = {
    copy(components = components.updated(component.ref, component))
  }

  def allComponents: Iterable[Component] = components.values

  def logicComponents: Iterable[CodeLogic] = allComponents.collect {
    case c: CodeLogic => c
  }

  def spriteComponents: Iterable[SpriteRenderer] = allComponents.collect {
    case c: SpriteRenderer => c
  }

}

object World {

  def empty(gameConfig: GameConfig): World = new World(Map.empty, Map.empty, gameConfig)

}
