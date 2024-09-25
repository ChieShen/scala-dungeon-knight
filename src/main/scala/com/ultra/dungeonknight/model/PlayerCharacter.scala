package com.ultra.dungeonknight.model

import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image

import scala.math.BigDecimal.{RoundingMode, double2bigDecimal}
import scala.util.Try
import scalikejdbc._
import com.ultra.dungeonknight.util.Database

class PlayerCharacter (_name: String) extends Entity with Database{
  level = 0
  name = _name
  var coins = 60
  var levelUpCost = 10
  var waveReached = 0
  var highestDamage = 0.0
  sprite = new Image(getClass.getResourceAsStream("/images/playercharacter/idle03.png"))

  skill = new BladeDance
  var skillPool = new ObservableBuffer[Skill]()
  skillPool += skill
  skillPool += new DrainingSlash
  skillPool += new VoidSlash

  var weapon: Weapon = new StarterSword
  weapon.owned = true
  var weaponLoadout = new ObservableBuffer[Weapon]()
  weaponLoadout += weapon
  weaponLoadout += new TruthSeeker
  weaponLoadout += new Oblivion

  for (n <- 1 to 10){
    idleImageList += new Image(getClass.getResourceAsStream(s"/images/playercharacter/idle/idle ($n).png"))
    walkingImageList += new Image(getClass.getResourceAsStream(s"/images/playercharacter/walking/walking ($n).png"))
    attackImageList += new Image(getClass.getResourceAsStream(s"/images/playercharacter/attack/attack ($n).png"))
  }


  levelUp()

  override def basicAttackDamage(target: Entity): Double = {
    val enemyEffectiveDefense = math.max(target.defense - (defensePenetration + weapon.defensePenetration),0)
    val damage = math.max(strength + weapon.strength - enemyEffectiveDefense, 0).setScale(1,RoundingMode.HALF_UP).toDouble
    damage
  }

  def levelUp(): Unit ={
    level += 1
    coins -= levelUpCost
    defensePenetration = 5 * level + 15
    strength = level * 3 + 30
    defense= level * 1.5 + 15
    maxHealth = 100 + level * 10
    health = maxHealth
    levelUpCost = level * 30
  }

  def save(): Try[Int] = {
    Try(DB autoCommit { implicit session =>
      sql"""
          insert into player (playerName,playerLevel, waveReached, weaponName, highestDamage) values
            (${name}, ${level}, ${waveReached}, ${weapon.name}, ${highestDamage})
        """.update.apply()
    })
  }
}

object PlayerCharacter extends Database{
  def apply(
             nameS: String,
             playerLevelI: Int,
             waveReachedI: Int,
             weaponNameS: String,
             highestDamageD: Double
           ): PlayerCharacter = {

    new PlayerCharacter(nameS){
      level = playerLevelI
      waveReached = waveReachedI
      weapon.name = weaponNameS
      highestDamage = highestDamageD
    }
  }

  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
      create table player (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        playerName varchar(64),
        playerLevel int,
        waveReached int,
        weaponName varChar(64),
        highestDamage double
      )
      """.execute.apply()
    }
  }

  def getAllPlayers: List[PlayerCharacter] = {
    DB readOnly { implicit session =>
      sql"select * from player".map(row => PlayerCharacter(row.string("playerName"),row.int("playerLevel"),
        row.int("waveReached"),row.string("weaponName"),row.double("highestDamage"))).list.apply()
    }
  }
}