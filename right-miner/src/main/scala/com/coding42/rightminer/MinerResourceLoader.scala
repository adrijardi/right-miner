package com.coding42.rightminer

import com.coding42.engine.{ResourceLoader, TextureLoader}

import scala.util.Try

/**
  * Loads all resources for the right miner game
  */
object MinerResourceLoader extends ResourceLoader[Resources] {

  override def apply: Try[Resources] = {
    for {
      player <- TextureLoader.newTexture("8_Bit_Mario.png")
      block <- TextureLoader.newTexture("block.jpg")
    } yield Resources(player, block)
  }

}
