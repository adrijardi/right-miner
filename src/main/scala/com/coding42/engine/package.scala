package com.coding42

import scala.util.Try

/**
  * Common types of the game engine
  */
package object engine {

  type ResourceLoader = () => Try[Resources]
  type Entity = (GameObject, Set[Component])
  type EntitiesLoader = (Resources) => Set[Entity]
}
