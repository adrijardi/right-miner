package com.coding42.engine

import scala.collection.mutable

/**
  * KeyManager that stores the processing time comes
  */
object KeyManager {

  private val downMap = new mutable.HashMap[Int, Any]
  private val upMap = new mutable.HashMap[Int, Any]

  def storeDownKey(key: Int): Unit = storeKey(downMap, key)

  def storeUpKey(key: Int): Unit = storeKey(upMap, key)

  private[this] def storeKey(map: mutable.HashMap[Int, Any], key: Int): Unit = {
    map.synchronized{
      map.put(key, 1)
    }
  }

  def invokeKeys(world: World): World = {
    val up = consumeUp()
    val worldAfterUp = up
      .foldLeft(world)( (worldRes, key) => invokeKey(key)(_.handleKeyUp(key))(worldRes) )

    consumeDown()
      .foldLeft(worldAfterUp)( (worldRes, key) => invokeKey(key)(_.handleKeyDown(key))(worldRes) )
  }

  private[this] def invokeKey(key: Int)(pressFn: (CodeLogic) => (World) => World)(world: World): World = {
    world.logicComponents
      .foldLeft(world)((w, l) => pressFn(l)(w)) // TODO change to traverse ?
  }

  private[this] def consumeDown(): Iterable[Int] = consumeKeys(downMap)

  private[this] def consumeUp(): Iterable[Int] = consumeKeys(upMap)

  private[this] def consumeKeys(map: mutable.HashMap[Int, Any]): Set[Int] = {
    map.synchronized {
      val result = map.keySet.toSet
      map.clear()
      result
    }
  }

}
