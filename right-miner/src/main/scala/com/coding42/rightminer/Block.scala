package com.coding42.rightminer

import com.coding42.engine._
import org.lwjgl.glfw.GLFW.{GLFW_KEY_A, GLFW_KEY_D, GLFW_KEY_S, GLFW_KEY_W}

/**
  * Player factory
  */
object Block {

  def apply(resources: Resources): Entity = {
    val block = GameObject("block", Transform(Position(100, 0, 100), Scale(16, 1, 30)))

    val components = Set[Component]( // TODO why this?
      SpriteRenderer(block.ref, resources.block),
      SphereCollider(block.ref, Position.zero, 20, trigger = true)
    )

    (block, components)
  }

}
