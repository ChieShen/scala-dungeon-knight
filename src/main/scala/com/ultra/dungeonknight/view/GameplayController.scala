package com.ultra.dungeonknight.view

import com.ultra.dungeonknight.MainApp
import com.ultra.dungeonknight.model.{BossMob, ChanceMob, Entity, Mob, NormalMob, PlayerCharacter}
import com.ultra.dungeonknight.util.Animation
import scalafx.animation.{KeyFrame, SequentialTransition, Timeline}
import scalafx.application.Platform
import scalafx.beans.property
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration
import scalafxml.core.macros.sfxml

import scala.math.BigDecimal.{RoundingMode, double2bigDecimal}
import scala.util.Random

@sfxml
class GameplayController(
                        private val waveLabel: Label,
                        private val coinsLabel: Label,
                        private val playerNameLabel: Label,
                        private val playerLevelLabel: Label,
                        private val mobNameLabel: Label,
                        private val mobLevelLabel: Label,
                        private val playerHealthLabel: Label,
                        private val mobHealthLabel: Label,
                        private val messageLabel: Label,
                        private val cooldownLabel: Label,
                        private var playerImageView: ImageView,
                        private val mobImageView: ImageView,
                        private val basicAttackButton: Button,
                        private val skillButton: Button,
                        private val playerHealthBar: Rectangle,
                        private val mobHealthBar: Rectangle
                        ){
  private var _playerCharacter: PlayerCharacter = null
  private var enemy: Mob = null
  private var skillCooldown = 0
  private var bossSkillCooldown = 0
  private val animation = new  Animation
  private var playerTimeLine: Timeline = null
  private var mobTimeline: Timeline = null
  private var buttonDisable: Timeline = null
  private var playerAttackSequence: SequentialTransition = null
  private var mobAttackSequence: SequentialTransition = null

  def playerCharacter = _playerCharacter
  def playerCharacter_=(playerCharacter: PlayerCharacter){
    _playerCharacter = playerCharacter
    playerNameLabel.text.value = _playerCharacter.name
    cooldownLabel.text.value = skillCooldown.toString
    coinsLabel.text <== new StringProperty(_playerCharacter.coins.toString)
    playerLevelLabel.text <== new StringProperty("Level: " +_playerCharacter.level.toString)
    cooldownLabel.text <== new StringProperty(skillCooldown.toString)
    playerTimeLine = animation.playIdleAnimation(_playerCharacter,playerImageView)
  }

  var wave = 0
  private var highestDamage = 0.0
  private var playerMessage = ""
  private var mobMessage = ""
  private val originalWidth = mobHealthBar.width.toDouble

  def startGame(): Unit = {
    playerTimeLine.play()
    nextWave()
  }

  def playerAttack(): Unit = {
    val damageDealt = _playerCharacter.attack(enemy)
    playerAttackSequence = animation.playAttackAnimation(_playerCharacter,enemy,playerImageView, mobImageView, originalWidth, mobHealthBar)
    playerAttackSequence.play()
    playerMessage = setMessage(_playerCharacter,enemy,"Basic Attack",damageDealt)
    skillCooldown = math.max(skillCooldown - 1,0)

    handleAttackOutcome(damageDealt)
  }

  def playerSkillAttack(): Unit = {
    val damageDealt = _playerCharacter.skill.activate(_playerCharacter,enemy).setScale(1, RoundingMode.HALF_UP).toDouble
    playerAttackSequence = animation.playAttackAnimation(_playerCharacter,enemy,playerImageView, mobImageView, originalWidth, mobHealthBar)
    playerAttackSequence.play()
    playerMessage = setMessage(_playerCharacter,enemy,_playerCharacter.skill.name,damageDealt)
    skillCooldown = _playerCharacter.skill.cooldown
    handleAttackOutcome(damageDealt)
  }

  private def handleAttackOutcome(damageDealt: Double): Unit = {
    disableButtons(Duration(5000))
    if(damageDealt > highestDamage){
      highestDamage = damageDealt
    }
    playerAttackSequence.onFinished =_ => {
      if (enemy.isDead) {
        _playerCharacter.coins += enemy.drops
        mobMessage = ""
        mobTimeline.stop()
        playerAttackSequence.stop()
        mobHealthBar.width.unbind()
        mobAttackSequence.stop()
        nextWave()
      } else if (enemy.isInstanceOf[BossMob] && bossSkillCooldown == 0) {
        val damageDealt = enemy.skill.activate(enemy, _playerCharacter).setScale(1, RoundingMode.HALF_UP).toDouble
        mobAttackSequence = animation.playAttackAnimation(enemy, _playerCharacter, mobImageView, playerImageView, originalWidth, playerHealthBar)
        mobAttackSequence.play()
        mobMessage = setMessage(enemy, _playerCharacter, enemy.skill.name, damageDealt)
        bossSkillCooldown = enemy.skill.cooldown
        mobAttackSequence.onFinished =_ =>{
          mobAttackSequence.stop()
          update()
        }
      } else {
        val damageDealt = enemy.attack(_playerCharacter)
        mobAttackSequence = animation.playAttackAnimation(enemy, _playerCharacter, mobImageView, playerImageView, originalWidth, playerHealthBar)
        mobAttackSequence.play()
        mobMessage = setMessage(enemy, _playerCharacter, "Basic Attack", damageDealt)
        if (enemy.isInstanceOf[BossMob]) {
          bossSkillCooldown = math.max(skillCooldown - 1, 0)
        }
        mobAttackSequence.onFinished =_ =>{
          mobAttackSequence.stop()
          update()
        }
      }
    }
  }

  private def nextWave():Unit ={
    val normalMobChance = 0.7
    val rng = new Random().nextDouble()
    wave += 1
    enemy = if (wave % 5 == 0) {
      new BossMob(wave)
    } else if (rng < normalMobChance) {
      new NormalMob(wave)
    } else {
      new ChanceMob(wave)
    }
    if (enemy.isInstanceOf[BossMob]){
      bossSkillCooldown = 0
    }
    mobTimeline = animation.playIdleAnimation(enemy,mobImageView)
    mobTimeline.play()
    mobHealthBar.width() = originalWidth
    update()
  }

  def update(): Unit = {
    waveLabel.text <== new StringProperty(wave.toString)
    coinsLabel.text <== new StringProperty(_playerCharacter.coins.toString)
    mobNameLabel.text <== new property.StringProperty(enemy.name)
    mobLevelLabel.text <== new StringProperty("Level: " + enemy.level.toString)
    mobHealthLabel.text <== new StringProperty(enemy.health.toString + "/" + enemy.maxHealth.toString)
    playerHealthLabel.text <== new StringProperty(_playerCharacter.health.toString + "/" + _playerCharacter.maxHealth.toString)
    playerLevelLabel.text <== new StringProperty("Level: " +_playerCharacter.level.toString)
    cooldownLabel.text <== new StringProperty(skillCooldown.toString)
    skillButton.text = _playerCharacter.skill.name
    messageLabel.text = playerMessage + "\n" + mobMessage
    checkGameEnded()
    updatePlayerHealthBar()
  }

  private def checkGameEnded(): Unit = {
    if (_playerCharacter.isDead){
      playerTimeLine.stop()
      mobTimeline.stop()
      playerAttackSequence.stop()
      mobAttackSequence.stop()
      buttonDisable.stop()

      _playerCharacter.highestDamage = highestDamage
      _playerCharacter.waveReached = wave
      MainApp.playerList += _playerCharacter
      _playerCharacter.save
      //show and wait error fix from
      // https://stackoverflow.com/questions/22463004/using-showandwait-in-the-onfinished-eventhandler-of-an-animation-doesnt-work
      Platform.runLater {
        val alert = new Alert(Alert.AlertType.Information) {
          initOwner(MainApp.stage)
          title = "Oh no! You have died."
          headerText = s"You have reached ${wave} and your highest damage is $highestDamage!"
          contentText = "Click on OK to access to the main menu"
        }.showAndWait()
      }
      MainApp.showMainMenu()
    }
  }

  private def updatePlayerHealthBar(): Unit = {
    playerHealthBar.width.unbind()
    playerHealthBar.width() = (_playerCharacter.health/_playerCharacter.maxHealth) * originalWidth
  }

  private def setMessage(user: Entity, target: Entity, attackType: String, damageDealt: Double): String ={
    s"${user.name} strike ${target.name} with $attackType, dealing $damageDealt damage!"
  }

  private def disableButtons(time: Duration): Unit = {
    basicAttackButton.disable = true
    skillButton.disable = true

    buttonDisable = new Timeline {
      keyFrames = Seq(
        KeyFrame(time, onFinished =_ =>{
          basicAttackButton.disable = false
          updateSkillButton()
        })
      )
    }
    buttonDisable.play()
  }

  private def updateSkillButton (): Unit = {
    if (skillCooldown > 0){
      skillButton.disable = true
    }else{
      skillButton.disable = false
    }
  }

  def characterClicked (action: ActionEvent): Unit = {
    _playerCharacter = MainApp.showCharacterInfoDialog(playerCharacter)
    update()
  }

  def skillClicked (action: ActionEvent): Unit = {
    val response = MainApp.showSkillShopDialog(playerCharacter)
    _playerCharacter = response._1
    if (response._2){
      skillCooldown = _playerCharacter.skill.cooldown
    }
    update()
  }

  def shopClicked (action: ActionEvent): Unit = {
    _playerCharacter = MainApp.showItemShopDialog(playerCharacter)
    update()
  }
}