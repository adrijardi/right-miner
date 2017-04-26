package com.coding42.rightminer

import com.coding42.engine._
import org.lwjgl.glfw.GLFW.{GLFW_KEY_A, GLFW_KEY_D, GLFW_KEY_S, GLFW_KEY_W}

/**
  * Player factory
  */
object Player {

  def apply(resources: Resources): Entity = {
    val player = GameObject("player", Transform(Position(0, 0, 0)))
    val components = Set(
      SpriteRenderer(player.ref, resources.player),

      new CodeLogic {

        override def gameObjectRef: GameObjectRef = player.ref

        override def handleKeyPressed(key: Int)(world: World): World = {
          val go = gameObject(world)
          val currentPos = go.transform.position
          key match {
            case GLFW_KEY_D => world.withGameObject(go.withPos(currentPos.copy(x = currentPos.x + 1)))
            case GLFW_KEY_A => world.withGameObject(go.withPos(currentPos.copy(x = currentPos.x - 1)))
            case GLFW_KEY_S => world.withGameObject(go.withPos(currentPos.copy(z = currentPos.z + 1)))
            case GLFW_KEY_W => world.withGameObject(go.withPos(currentPos.copy(z = currentPos.z - 1)))
            case _ => world
          }
        }

      }
    )

    (player, components)
  }
}
