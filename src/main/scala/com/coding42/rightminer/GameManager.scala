package com.coding42.rightminer

import com.coding42.engine._
import org.lwjgl.glfw.GLFW._

/**
  * Game object that handles generic actions
  */
object GameManager {

  def apply(resources: Resources): Entity = {
    val manager = GameObject("gameManager", Transform(Position(0, 0, 0)))

    val components = Set[Component](
      new CodeLogic {

        override def gameObjectRef: GameObjectRef = manager.ref

        override def handleKeyPressed(key: Int)(world: World): World = {
          key match {
            case GLFW_KEY_ESCAPE => world.copy(gameConfig = world.gameConfig.copy(close = true))
            case _ => world
          }
        }

      }
    )

    (manager, components)
  }
}