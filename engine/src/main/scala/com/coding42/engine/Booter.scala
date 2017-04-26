package com.coding42.engine

import org.lwjgl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

import scala.util.control.NonFatal
import scala.util.{Failure, Success}

class Booter[Resources](config: GameConfig, resourceLoader: ResourceLoader[Resources], entitiesLoader: EntitiesLoader[Resources]) {
  private var window = 0L
  private var world: World = World.empty(config)

  def run(): Unit = {
    System.out.println("Hello LWJGL " + Version.getVersion + "!")
    init()
    loop()
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  private def init() = { // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set
    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit) throw new IllegalStateException("Unable to initialize GLFW")
    // Configure GLFW
    glfwDefaultWindowHints() // optional, the current window hints are already the default

    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation

    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

    // Create the window
    val config = world.gameConfig
    window = glfwCreateWindow(config.screenWidth, config.screenHeight, config.windowName, NULL, NULL)
    if (window == NULL) throw new RuntimeException("Failed to create the GLFW window")

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
      def foo(window: Long, key: Int, scancode: Int, action: Int, mods: Int) = action match {
//        case GLFW_PRESS => invokeKey(_.handleKeyPressed(key))
        case GLFW_PRESS => invokeKey(_.handleKeyDown(key))
        case GLFW_RELEASE => invokeKey(_.handleKeyUp(key))
        case n => println(s"Not handling event of type $n")
      }

      foo(window, key, scancode, action, mods)
    })
    // Get the thread stack and push a new frame
    val stack = stackPush
    try {
      val pWidth = stack.mallocInt(1)
      // int*
      val pHeight = stack.mallocInt(1)
      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight)
      // Get the resolution of the primary monitor
      val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
      // Center the window
      glfwSetWindowPos(window, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)
      // the stack frame is popped automatically
    } finally {
      if (stack != null) stack.close()
    }
    // Make the OpenGL context current
    glfwMakeContextCurrent(window)
    // Enable v-sync
    glfwSwapInterval(1)
    // Make the window visible
    glfwShowWindow(window)
  }

  private def invokeKey(pressFn: (CodeLogic) => (World) => World) = {
    world = world.allComponents.collect {
      case c: CodeLogic => c
    }.foldLeft(world)((w, l) => pressFn(l)(w)) // TODO change to traverse ?
  }

  private def loop() = { // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities

    // enable textures since we're going to use these for our sprites// enable textures since we're going to use these for our sprites
    GL11.glEnable(GL11.GL_TEXTURE_2D)

    // disable the OpenGL depth test since we're rendering 2D graphics
    GL11.glDisable(GL11.GL_DEPTH_TEST)

    GL11.glMatrixMode(GL11.GL_PROJECTION)
    GL11.glLoadIdentity()

    val config = world.gameConfig
    GL11.glOrtho(0, config.screenWidth, config.screenHeight, 0, -1, 1)

    // Set the clear color
    glClearColor(.3f, 0.8f, 0.3f, 0.0f)

    resourceLoader() match {
      case Success(resources) =>

        val entities = entitiesLoader(resources).unzip
        val gameObjects = entities._1
        val components = entities._2.flatten
        world = components.foldLeft(world)( (worldRes, c) => worldRes.withComponent(c) )

        world = gameObjects.foldLeft(world)( (worldRes, go) => worldRes.withGameObject(go) )

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( {
          !world.gameConfig.close
        }) {
          glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer

          // Execute onUpdate on all logic components
          world = world.logicComponents.foldLeft(world)( (worldRes, c) => c.onUpdate(1f)(worldRes) )

          // Handle collisions
          // TODO make simpler
          try {
            world = Collider.collisions(world.colliderComponents)(world)
              .foldLeft(world) { case (worldRes, (goRef, collisions)) =>
                collisions.foldLeft(worldRes)((worldCollision, collision) =>
                  worldCollision.logicComponents(goRef)
                    .foldLeft(worldRes)((worldResLogic, logic) => logic.onCollisionEnter(collision)(worldResLogic))
                )
              }
          } catch {
            case NonFatal(t) =>
              println(t)
          }

          // Execute draw on all Sprite renderers components
          world.spriteComponents.foreach(_.draw(world))

          glfwSwapBuffers(window) // swap the color buffers

          // Poll for window events. The key callback above will only be
          // invoked during this call.
          glfwPollEvents()
        }

      case Failure(NonFatal(t)) =>
        System.err.println(s"Could not load resources, cause: $t")
    }
  }

}
