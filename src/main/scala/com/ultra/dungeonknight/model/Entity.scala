package com.ultra.dungeonknight.model

import scalafx.scene.image.Image

import scala.collection.mutable.ArrayBuffer
import scala.math.BigDecimal.{RoundingMode, double2bigDecimal}

abstract class Entity{
  var name = ""
  var level: Int= 0
  var strength: Double = 0
  var defense: Double = 0
  var maxHealth: Double = 0
  var health: Double = maxHealth
  var defensePenetration: Int = 0
  var skill: Skill = new Cleave
  var isDead = false
  var idleImageList = ArrayBuffer[Image]()
  var walkingImageList = ArrayBuffer[Image]()
  var attackImageList = ArrayBuffer[Image]()
  var sprite = new Image(getClass.getResourceAsStream("/images/icon.png"))

  def initializeStats(): Unit ={

  }

  def basicAttackDamage(target: Entity): Double ={
    val enemyEffectiveDefense = math.max(target.defense - defensePenetration,0)
    val damage = math.max(strength - enemyEffectiveDefense, 0).setScale(1,RoundingMode.HALF_UP).toDouble
    damage
  }

  def attack(target: Entity): Double = {
    val damage = basicAttackDamage(target)
    target.health = (target.health - damage).setScale(1, RoundingMode.HALF_UP).toDouble
    if (target.health <= 0) {
      target.health = 0
      target.isDead = true
    }
    damage
  }
}

