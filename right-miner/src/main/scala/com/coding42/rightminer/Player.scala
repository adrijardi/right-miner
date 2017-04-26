package com.coding42.rightminer

import com.coding42.engine._
import org.lwjgl.glfw.GLFW.{GLFW_KEY_A, GLFW_KEY_D, GLFW_KEY_S, GLFW_KEY_W}

/**
  * Player factory
  */
object Player {

  case class PlayerMovement(ref: ComponentRef, gameObjectRef: GameObjectRef, xMov: Int, zMov: Int) extends CodeLogic {

    val speed = 3f

    override def handleKeyDown(key: Int)(world: World): World = {
      key match {
        case GLFW_KEY_D => world.withComponent(copy(xMov = 1))
        case GLFW_KEY_A => world.withComponent(copy(xMov = -1))
        case GLFW_KEY_S => world.withComponent(copy(zMov = 1))
        case GLFW_KEY_W => world.withComponent(copy(zMov = -1))
        case _ => world
      }
    }


    override def handleKeyUp(key: Int)(world: World): World = {
      (key, xMov, zMov) match {
        case (GLFW_KEY_D, 1, _) => world.withComponent(copy(xMov = 0))
        case (GLFW_KEY_A, -1, _) => world.withComponent(copy(xMov = 0))
        case (GLFW_KEY_S, _, 1) => world.withComponent(copy(zMov = 0))
        case (GLFW_KEY_W, _, -1) => world.withComponent(copy(zMov = 0))
        case _ => world
      }
    }

    override def onUpdate(deltaTime: Float)(world: World): World = {
      val go = gameObject(world)
      val position = go.transform.position

      def mov(axisMov: Int) = {
        axisMov match {
          case x if x < 0 => speed * deltaTime * -1
          case x if x > 0 => speed * deltaTime
          case _ => 0f
        }
      }

      val newPos = position.copy(x = position.x + mov(xMov), z = position.z + mov(zMov))
      world.withGameObject(go.withPos(newPos))
    }
  }

  def apply(resources: Resources): Entity = {
    val player = GameObject("player", Transform(Position(0, 0, 0), Scale(16, 1, 30)))

    val components = Set[Component]( // TODO why this?
      SpriteRenderer(player.ref, resources.player),
      PlayerMovement(ComponentRef(), player.ref, 0, 0)
    )

    (player, components)
  }

}
