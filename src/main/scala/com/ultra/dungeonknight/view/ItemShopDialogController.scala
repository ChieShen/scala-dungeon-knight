package com.ultra.dungeonknight.view

import com.ultra.dungeonknight.model.{PlayerCharacter, Weapon}
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, Label, TableColumn, TableView}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class ItemShopDialogController(
                              private val weaponTable: TableView[Weapon],
                              private val weaponNameColumn: TableColumn[Weapon, String],
                              private val nameLabel: Label,
                              private val coinsLabel: Label,
                              private val costLabel: Label,
                              private val strengthLabel: Label,
                              private val defensePenetrationLabel: Label,
                              private val descriptionLabel: Label,
                              private val unlockButton: Button,
                              private val equipButton: Button
                              ){
  var dialogStage: Stage = null
  private var _playerCharacter: PlayerCharacter = null

  def playerCharacter: PlayerCharacter = _playerCharacter
  def playerCharacter_=(playerCharacter: PlayerCharacter){
    _playerCharacter = playerCharacter

    update()
  }

  //code from https://imailsunwayedu.sharepoint.com/:o:/r/sites/PRG2104OOPAPR2024/SiteAssets/PRG2104%20OOP%20APR%202024%20Notebook?d=w857f797590d9401cabbabcf5c37eeac8&csf=1&web=1&e=8YLKBj
  import scalafx.Includes._
  def update(): Unit = {
    coinsLabel.text <== new StringProperty(_playerCharacter.coins.toString)
    weaponTable.items = _playerCharacter.weaponLoadout
    weaponNameColumn.cellValueFactory = {
      x => new StringProperty(x.value.name)
    }
    showWeaponDetails(None)
    weaponTable.selectionModel().selectedItem.onChange { (_, _, selectedWeapon) =>
      showWeaponDetails(Option(selectedWeapon))
    }
  }

  private def showWeaponDetails(weapon: Option[Weapon]): Unit = {
    weapon match {
      case Some(weapon) =>
        nameLabel.text <== new StringProperty(weapon.name)
        costLabel.text <== new StringProperty(weapon.cost.toString)
        strengthLabel.text <== new StringProperty(weapon.strength.toString)
        defensePenetrationLabel.text <== new StringProperty(weapon.defensePenetration.toString)
        descriptionLabel.text <== new StringProperty(weapon.description)
        updateButtonStates(weapon)
      case None =>
        nameLabel.text.unbind()
        costLabel.text.unbind()
        strengthLabel.text.unbind()
        defensePenetrationLabel.text.unbind()
        descriptionLabel.text.unbind()
        nameLabel.text = ""
        costLabel.text = ""
        strengthLabel.text = ""
        defensePenetrationLabel.text = ""
        descriptionLabel.text = ""
    }
  }

  private def updateButtonStates(weapon: Weapon): Unit = {
    if (weapon.owned == true && _playerCharacter.weapon != weapon) {
      equipButton.visible = true
      unlockButton.visible = false
    } else if (weapon.owned == true && _playerCharacter.weapon == weapon) {
      equipButton.visible = false
      unlockButton.visible = false
    }else{
      equipButton.visible = false
      unlockButton.visible = true
    }
  }

  def unlockClicked(action: ActionEvent): Unit = {
    val selectedWeapon = weaponTable.selectionModel().selectedItem.value
    if (_playerCharacter.coins >= selectedWeapon.cost){
      _playerCharacter.coins -= selectedWeapon.cost
      selectedWeapon.owned = true
      update()
      showWeaponDetails(Option(selectedWeapon))
    }else{
      val alert = new Alert(AlertType.Error) {
        initOwner(dialogStage)
        title = "Insufficient Coins"
        headerText = s"Not enough coins to unlock ${selectedWeapon.name}."
        contentText = s"You need ${selectedWeapon.cost - _playerCharacter.coins} more coins to unlock${selectedWeapon.name}."
      }
      alert.showAndWait()
    }
  }

  def equipClicked(action: ActionEvent): Unit = {
    val selectedIndex = weaponTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      val selectedWeapon = weaponTable.selectionModel().selectedItem.value
      _playerCharacter.weapon = selectedWeapon
      update()
      showWeaponDetails(Option(selectedWeapon))
    }else{
      new Alert(AlertType.Error){
        initOwner(dialogStage)
        title       = "No Selection"
        headerText  = "No Weapon Selected"
        contentText = "Please select a weapon in the table."
      }.showAndWait()
    }
  }

  def exitClicked(action: ActionEvent): Unit = {
    dialogStage.close()
  }
}