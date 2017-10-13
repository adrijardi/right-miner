package com.coding42.engine

package object util {

  def isBetween(value: Float, min: Float, max: Float): Boolean = value >= min && value <= max
}
