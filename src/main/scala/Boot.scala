import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._
import org.newdawn.slick.opengl.TextureLoader
import org.newdawn.slick.util.ResourceLoader


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
    window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL)
    if (window == NULL) throw new RuntimeException("Failed to create the GLFW window")
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
      def foo(window: Long, key: Int, scancode: Int, action: Int, mods: Int) = {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
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

  private def loop() = { // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities
    // Set the clear color
    glClearColor(.3f, 0.8f, 0.3f, 0.0f)
    sprite
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while ( {
      !glfwWindowShouldClose(window)
    }) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer

      glfwSwapBuffers(window) // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents()
    }
  }

  def sprite = {
    import org.lwjgl.opengl.GL11
    val texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("8_Bit_Mario.png"))

    val height = 20
    val width = 16

    GL11.glBegin(GL11.GL_QUADS)
    GL11.glTexCoord2f(0, 0)
    GL11.glVertex2f(0, 0)
    GL11.glTexCoord2f(0, texture.getHeight)
    GL11.glVertex2f(0, height)
    GL11.glTexCoord2f(texture.getWidth, texture.getHeight)
    GL11.glVertex2f(width, height)
    GL11.glTexCoord2f(texture.getWidth, 0)
    GL11.glVertex2f(width, 0)

  }

  run()
}
