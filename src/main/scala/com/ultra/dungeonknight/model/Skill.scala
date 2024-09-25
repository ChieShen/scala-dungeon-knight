package com.ultra.dungeonknight.model

import scala.math.BigDecimal.{RoundingMode, double2bigDecimal}

abstract class Skill{
  var name = ""
  var cooldown = 0
  var description = ""
  var skillLevel = 1
  var cost = 0
  var skillLevelCap = 5

  def calculateDamage(user: Entity, target: Entity): Double

  def applyDamage(target: Entity, damage: Double): Unit = {
    target.health = (target.health - damage).setScale(1, RoundingMode.HALF_UP).toDouble
    if (target.health <= 0) {
      target.health = 0
      target.isDead = true
    }
  }

  def activate(user: Entity, target: Entity): Double = {
    val damage = calculateDamage(user, target)
    applyDamage(target, damage)
    damage
  }

  def upgrade(): Unit = {

  }
}

class BladeDance extends Skill{
  name = "Blade Dance"
  cooldown = 6
  cost = 300
  description = "Performs a series of fast strikes. Add one extra strike with each skill upgrade."

  override def calculateDamage(user: Entity, target: Entity): Double = {
    val baseDamage = user.basicAttackDamage(target) * 0.5
    (skillLevel + 2) * baseDamage
  }

  override def upgrade(): Unit = {
    skillLevel += 1
    cost += 300
  }
}

class DrainingSlash extends Skill{
  name = "Draining Slash"
  cooldown = 5
  cost = 400
  description = "Performs a strong attack. Recover 40% of the damage dealt. Add 2% recovery with each skill upgrade."

  override def calculateDamage(user: Entity, target: Entity): Double = {
    val damage = user.basicAttackDamage(target) * 1.5
    if (user.health < user.maxHealth) {
      val healthRecovery = (damage * (0.4 + 0.02 * skillLevel)).setScale(1, RoundingMode.HALF_UP).toDouble
      if (user.health + healthRecovery <= user.maxHealth) {
        user.health += healthRecovery
      } else {
        user.health = user.maxHealth
      }
    }
    damage
  }

  override def upgrade(): Unit = {
    skillLevel += 1
    cost += 400
  }
}

class VoidSlash extends Skill{
  name = "Void Slash"
  cooldown = 6
  cost = 700
  description = "Performs a powerful attack that ignores the defense of the enemy. Decrease cooldown by 1 with each skill upgrade."

  override def calculateDamage(user: Entity, target: Entity): Double = {
    val targetOriginalDefense = target.defense
    target.defense = 0
    val damage = 3 * user.basicAttackDamage(target)
    target.defense = targetOriginalDefense
    damage
  }

  override def upgrade(): Unit = {
    skillLevel += 1
    cooldown -= 1
    cost += 700
  }
}

class Cleave extends Skill{
  name = "Cleave"
  cooldown = 5
  description = "Deal damage to target enemy equal to 50% of their max health"

  override def calculateDamage(user: Entity, target: Entity): Double = {
    target.maxHealth * 0.5
  }
}