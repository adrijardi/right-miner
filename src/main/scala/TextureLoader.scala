import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import scala.util.Try

object TextureLoader {

  private val BYTES_PER_PIXEL = 4

  def newTexture(path: String): Try[Texture] = {
    Try {
      val IS = getClass.getResourceAsStream(path)
      val BAOS = new ByteArrayOutputStream
      var read1 = IS.read
      while ( {
        read1 != -1
      }) {
        BAOS.write(read1)
        read1 = IS.read
      }
      val textureBA = BAOS.toByteArray
      BAOS.close()
      val textureBI = ImageIO.read(new ByteArrayInputStream(textureBA))
      loadTexture(textureBI)
    }
  }

  private[this] def loadTexture(image: BufferedImage): Texture = {
    val pixels = new Array[Int](image.getWidth * image.getHeight)
    image.getRGB(0, 0, image.getWidth, image.getHeight, pixels, 0, image.getWidth)
    val buffer = BufferUtils.createByteBuffer(image.getWidth * image.getHeight * BYTES_PER_PIXEL)
    //4 for RGBA, 3 for RGB
    var y = 0
    while ( {
      y < image.getHeight
    }) {
      var x = 0
      while ( {
        x < image.getWidth
      }) {
        val pixel = pixels(y * image.getWidth + x)
        buffer.put(((pixel >> 16) & 0xFF).toByte) // Red component

        buffer.put(((pixel >> 8) & 0xFF).toByte) // Green component

        buffer.put((pixel & 0xFF).toByte) // Blue component

        buffer.put(((pixel >> 24) & 0xFF).toByte) // Alpha component. Only for RGBA


        {
          x += 1; x - 1
        }
      }

      {
        y += 1; y - 1
      }
    }
    buffer.flip //FOR THE LOVE OF GOD DO NOT FORGET THIS

    // You now have a ByteBuffer filled with the color data of each pixel.
    // Now just create a texture ID and bind it. Then you can newTexture it using
    // whatever OpenGL method you want, for example:
    val textureID = GL11.glGenTextures //Generate texture ID
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID) //Bind texture ID

    //Setup wrap mode
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
    //Setup texture scaling filtering
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
    //Send texel data to OpenGL
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth, image.getHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)

    //Return the texture ID so we can bind it later again
    Texture(textureID, image.getHeight, image.getWidth)
  }
}
