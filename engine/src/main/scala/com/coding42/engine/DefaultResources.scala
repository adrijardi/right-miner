package com.coding42.engine

object DefaultResources {

  val debugTexture: Texture = TextureLoader.newTexture("greenDot.png").getOrElse(throw new Exception("Cannot load texture 'greenDot.png'"))

}
