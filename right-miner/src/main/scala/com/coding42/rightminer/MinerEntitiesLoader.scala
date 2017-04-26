package com.coding42.rightminer

import com.coding42.engine.{Component, EntitiesLoader, GameObject}

/**
  * Creates all the entities in the game
  */
object MinerEntitiesLoader extends EntitiesLoader[Resources] {

  override def apply(resources: Resources): Set[(GameObject, Set[Component])] =
    Set(GameManager(resources), Player(resources))

}
