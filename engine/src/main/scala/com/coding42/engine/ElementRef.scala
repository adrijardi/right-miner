package com.coding42.engine

import java.util.concurrent.atomic.AtomicInteger

import scala.reflect.ClassTag
import scala.reflect._

/**
  * Identifiers generator
  */
object ElementRef {

  private[this] var counters: Map[Class[_], AtomicInteger] = Map.empty

  // TODO force calling this
  def apply[T](c: Int => T): T = {
    c(getOrCreateCounter.incrementAndGet())
  }

  // TODO this is quite messy
  private def getOrCreateCounter[T : ClassTag]: AtomicInteger = {
    val clazz = classTag[T].runtimeClass
    var counter = counters.get(clazz)

    if (counter.isEmpty) {
      synchronized {
        counter = counters.get(clazz)
        if (counter.isEmpty) {
          counter = Some(new AtomicInteger(0))
          counters = counters + (clazz -> counter.get)
        }
      }
    }

    counter.get
  }

}
