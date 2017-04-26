package com.coding42.rightminer

import com.coding42.engine.{ResourceLoader, Resources, TextureLoader}

import scala.util.Try

/**
  * Loads all resources for the right miner game
  */
object MinerResourceLoader extends ResourceLoader {

  override def apply: Try[Resources] = {
    val texture = TextureLoader.newTexture("8_Bit_Mario.png")
    texture.map(Resources)
  }

}
