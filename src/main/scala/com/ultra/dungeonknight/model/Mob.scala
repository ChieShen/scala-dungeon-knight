package com.ultra.dungeonknight.model

import scalafx.scene.image.Image

abstract class Mob(val wave: Int) extends Entity {
  level = wave
  var drops = 0
  initializeStats()

  override def initializeStats(): Unit = {
    strength = level * 2 + 30
    defense = level * 2 + 20
    maxHealth = 100 + level * 8
    health = maxHealth
    drops = level * 10
  }
}

class NormalMob(_wave: Int) extends Mob(_wave) {
  name = "Golem"
  initializeStats()
  sprite = new Image(getClass.getResourceAsStream("/images/normalmob/idle03.png"))

  for (n <- 1 to 10){
    idleImageList += new Image(getClass.getResourceAsStream(s"/images/normalmob/idle/idle ($n).png"))
    walkingImageList += new Image(getClass.getResourceAsStream(s"/images/normalmob/walking/walking ($n).png"))
    attackImageList += new Image(getClass.getResourceAsStream(s"/images/normalmob/attack/attack ($n).png"))
  }
}

class ChanceMob(_wave: Int) extends Mob(_wave) {
  name = "Goblin"
  initializeStats()
  drops = 20 * _wave
  sprite = new Image(getClass.getResourceAsStream("/images/chancemob/idle03.png"))
  for (n <- 1 to 10){
    idleImageList += new Image(getClass.getResourceAsStream(s"/images/chancemob/idle/idle ($n).png"))
    walkingImageList += new Image(getClass.getResourceAsStream(s"/images/chancemob/walking/walking ($n).png"))
    attackImageList += new Image(getClass.getResourceAsStream(s"/images/chancemob/attack/attack ($n).png"))
  }
}

class BossMob(_wave: Int) extends Mob (_wave){
  name = "Doom"
  strength = _wave * 3 + 30
  defense = _wave * 3 + 30
  maxHealth = 100 + _wave * 10
  health = maxHealth
  drops = 50 * _wave
  defensePenetration = 2 * _wave
  sprite = new Image(getClass.getResourceAsStream("/images/bossmob/idle03.png"))
  for (n <- 1 to 10){
    idleImageList += new Image(getClass.getResourceAsStream(s"/images/bossmob/idle/idle ($n).png"))
    walkingImageList += new Image(getClass.getResourceAsStream(s"/images/bossmob/walking/walking ($n).png"))
    attackImageList += new Image(getClass.getResourceAsStream(s"/images/bossmob/attack/attack ($n).png"))
  }
}