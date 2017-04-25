import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

import scala.util.Try

object Boot extends App {
  private var window = 0L

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

  val width = 300
  val height = 300

  var posX = 0
  var posY = 0

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
    window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL)
    if (window == NULL) throw new RuntimeException("Failed to create the GLFW window")

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
      def foo(window: Long, key: Int, scancode: Int, action: Int, mods: Int) = {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
        if (key == GLFW_KEY_D) posX += 1
        if (key == GLFW_KEY_A) posX -= 1
        if (key == GLFW_KEY_S) posY += 1
        if (key == GLFW_KEY_W) posY -= 1
      }

      foo(window, key, scancode, action, mods)
    })
    // Get the thread stack and push a new frame
    try {
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
        // the stack frame is popped automatically} finally {
        if (stack != null) stack.close()
      }
    }
    // Make the OpenGL context current
    glfwMakeContextCurrent(window)
    // Enable v-sync
    glfwSwapInterval(1)
    // Make the window visible
    glfwShowWindow(window)
  }

  private def loadResouces: Try[Resources] = {
    val texture = TextureLoader.newTexture("8_Bit_Mario.png")
    texture.map(Resources)
  }

  private def loop() = { // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities

    loadResouces map { resources =>
      // enable textures since we're going to use these for our sprites// enable textures since we're going to use these for our sprites
      GL11.glEnable(GL11.GL_TEXTURE_2D)

      // disable the OpenGL depth test since we're rendering 2D graphics
      GL11.glDisable(GL11.GL_DEPTH_TEST)

      GL11.glMatrixMode(GL11.GL_PROJECTION)
      GL11.glLoadIdentity()

      GL11.glOrtho(0, width, height, 0, -1, 1)

      // Set the clear color
      glClearColor(.3f, 0.8f, 0.3f, 0.0f)
      // Run the rendering loop until the user has attempted to close
      // the window or has pressed the ESCAPE key.
      while ( {
        !glfwWindowShouldClose(window)
      }) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        draw(resources.player, posX, posY)
        glfwSwapBuffers(window) // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents()
      }
    }

  }

  import org.lwjgl.opengl.GL11

  def draw(texture: Texture, x: Int, y: Int): Unit = {

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id)

    // store the current model matrix
    GL11.glPushMatrix()
    // bind to the appropriate texture for this sprite
//    texture.bind
    // translate to the right location and prepare to draw
    GL11.glTranslatef(x, y, 0)
    GL11.glColor3f(1, 1, 1)
    // draw a quad textured to match the sprite
    GL11.glBegin(GL11.GL_QUADS)
    GL11.glTexCoord2f(0, 0)
    GL11.glVertex2f(0, 0)
    GL11.glTexCoord2f(0, texture.height)
    GL11.glVertex2f(0, height)
    GL11.glTexCoord2f(texture.width, texture.height)
    GL11.glVertex2f(width, height)
    GL11.glTexCoord2f(texture.width, 0)
    GL11.glVertex2f(width, 0)

    GL11.glEnd()
    // restore the model view matrix to prevent contamination
    GL11.glPopMatrix()
  }

  run()
}
