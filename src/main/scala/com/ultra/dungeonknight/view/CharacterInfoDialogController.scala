package com.ultra.dungeonknight.view

import com.ultra.dungeonknight.model.PlayerCharacter
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, Label}
import scalafx.scene.image.ImageView
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class CharacterInfoDialogController(
                                     private val nameLabel: Label,
                                     private val levelLabel: Label,
                                     private val weaponLabel: Label,
                                     private val coinsLabel: Label,
                                     private val maxHealthLabel: Label,
                                     private val strengthLabel: Label,
                                     private val defenseLabel: Label,
                                     private val defensePenetrationLabel: Label,
                                     private val levelUpCostLabel: Label,
                                     private val playerImageView: ImageView
                                   ) {
  var dialogStage: Stage = null
  private var _playerCharacter: PlayerCharacter = null

  def playerCharacter: PlayerCharacter = _playerCharacter
  def playerCharacter_=(playerCharacter: PlayerCharacter): Unit = {
    _playerCharacter = playerCharacter
    nameLabel.text.value = _playerCharacter.name
    playerImageView.image = _playerCharacter.sprite
    update()
  }

  def levelUpClicked(action: ActionEvent): Unit = {
    if (_playerCharacter.coins >= _playerCharacter.levelUpCost) {
      _playerCharacter.levelUp()
      update()
    } else {
      val alert = new Alert(AlertType.Error) {
        initOwner(dialogStage)
        title = "Insufficient Coins"
        headerText = "Not enough coins to level up."
        contentText = s"You need ${_playerCharacter.levelUpCost - _playerCharacter.coins} more coins to level up."
      }
      alert.showAndWait()
    }
  }

  def update(): Unit = {
    coinsLabel.text.value = _playerCharacter.coins.toString
    levelLabel.text.value = _playerCharacter.level.toString
    weaponLabel.text.value = _playerCharacter.weapon.name.toString
    maxHealthLabel.text.value = _playerCharacter.maxHealth.toString
    strengthLabel.text.value = _playerCharacter.strength.toString
    defenseLabel.text.value = _playerCharacter.defense.toString
    defensePenetrationLabel.text.value = _playerCharacter.defensePenetration.toString
    levelUpCostLabel.text.value = _playerCharacter.levelUpCost.toString
  }

  def exitClicked(action: ActionEvent): Unit = {
    dialogStage.close()
  }
}
