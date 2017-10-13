package com.coding42.rightminer

import com.coding42.engine.{Booter, GameConfig}

/**
  * Main class for the right miner game
  */
object Boot extends Booter(GameConfig(300, 300, "Right miner!", debug = true), MinerResourceLoader, MinerEntitiesLoader) with App {
  run()
}
