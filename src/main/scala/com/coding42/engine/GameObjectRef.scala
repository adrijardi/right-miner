package com.coding42.engine

import java.util.concurrent.atomic.AtomicInteger

/**
  * Reference to a game object that doesn't change between instances
  */
case class GameObjectRef(ref: Int)

object GameObjectRef {

  private[this] val counter: AtomicInteger = new AtomicInteger(0)

  // TODO force calling this
  def apply(): GameObjectRef = {
    new GameObjectRef(counter.incrementAndGet())
  }
}
