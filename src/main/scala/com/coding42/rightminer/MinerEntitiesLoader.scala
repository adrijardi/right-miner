package com.coding42.rightminer

import com.coding42.engine.{Component, EntitiesLoader, GameObject, Resources}

/**
  * Creates all the entities in the game
  */
object MinerEntitiesLoader extends EntitiesLoader {

  override def apply(resources: Resources): Set[(GameObject, Set[Component])] =
    Set(Player(resources))

}
