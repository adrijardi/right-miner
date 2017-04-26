package com.coding42.engine

/**
  * 3D position
  */
case class Position(x: Float, y: Float, z: Float) {

  def +(other: Position): Position = {
    Position(x + other.x, y + other.y, z + other.z)
  }

}

object Position {

  val zero: Position = Position(0,0,0)

  def distance(a: Position, b: Position): Float = {
    import Math._
    val x = pow(a.x, 2) - pow(b.x, 2)
    val y = pow(a.y, 2) - pow(b.y, 2)
    val z = pow(a.z, 2) - pow(b.z, 2)
    sqrt(x + y + z).toFloat
  }

}