package com.coding42.engine

/**
  * Reference to a component that doesn't change between instances
  */
case class ComponentRef(ref: Int)

object ComponentRef {

  // TODO force calling this
  def apply(): ComponentRef = ElementRef(ComponentRef(_))
}
