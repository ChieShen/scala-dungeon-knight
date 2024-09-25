package com.ultra.dungeonknight.view

import com.ultra.dungeonknight.model.{PlayerCharacter, Skill}
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, Label, TableColumn, TableView}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class SkillShopDialogController(
                                 private val skillTable: TableView[Skill],
                                 private val skillNameColumn: TableColumn[Skill, String],
                                 private val coinsLabel: Label,
                                 private val skillNameLabel: Label,
                                 private val skillLevelLabel: Label,
                                 private val costLabel: Label,
                                 private val cooldownLabel: Label,
                                 private val descriptionLabel: Label,
                                 private val upgradeButton: Button,
                                 private val equipButton: Button
                               ){
  var dialogStage: Stage = null
  private var _playerCharacter: PlayerCharacter = null
  private var currentSkill: Skill = null

  def playerCharacter: PlayerCharacter = _playerCharacter
  def playerCharacter_=(playerCharacter: PlayerCharacter){
    _playerCharacter = playerCharacter
    currentSkill = _playerCharacter.skill
    update()
  }

  var skillChanged = false

  //code from https://imailsunwayedu.sharepoint.com/:o:/r/sites/PRG2104OOPAPR2024/SiteAssets/PRG2104%20OOP%20APR%202024%20Notebook?d=w857f797590d9401cabbabcf5c37eeac8&csf=1&web=1&e=8YLKBj
  import scalafx.Includes._
  def update(): Unit = {
    coinsLabel.text <== new StringProperty(_playerCharacter.coins.toString)
    skillTable.items = _playerCharacter.skillPool
    skillNameColumn.cellValueFactory = {
      x => new StringProperty(x.value.name)
    }
    showSkillDetails(None)
    skillTable.selectionModel().selectedItem.onChange { (_, _, selectedSkill) =>
      showSkillDetails(Option(selectedSkill))
    }
  }

  private def showSkillDetails(skill: Option[Skill]): Unit = {
    skill match {
      case Some(skill) =>
        skillNameLabel.text <== new StringProperty(skill.name)
        skillLevelLabel.text <== new StringProperty(skill.skillLevel.toString)
        costLabel.text <== new StringProperty(skill.cost.toString)
        cooldownLabel.text <== new StringProperty(skill.cooldown.toString)
        descriptionLabel.text <== new StringProperty(skill.description)
        updateButtonStates(skill)
      case None =>
        skillNameLabel.text.unbind()
        skillLevelLabel.text.unbind()
        costLabel.text.unbind()
        cooldownLabel.text.unbind()
        descriptionLabel.text.unbind()
        skillNameLabel.text = ""
        skillLevelLabel.text = ""
        costLabel.text = ""
        cooldownLabel.text = ""
        descriptionLabel.text = ""
    }
  }

  private def updateButtonStates(skill: Skill): Unit = {
    if (skill.skillLevel == skill.skillLevelCap) {
      upgradeButton.text = "Maxed"
      upgradeButton.disable = true
    } else {
      upgradeButton.text = "Upgrade"
      upgradeButton.disable = false
    }

    if (skill == _playerCharacter.skill) {
      equipButton.text = "Equipped"
      equipButton.disable = true
    } else {
      equipButton.text = "Equip"
      equipButton.disable = false
    }
  }

  def upgradeClicked(action: ActionEvent): Unit = {
    val selectedIndex = skillTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0){
      val selectedSkill = skillTable.selectionModel().selectedItem.value
      if (_playerCharacter.coins >= selectedSkill.cost) {
        _playerCharacter.coins -= selectedSkill.cost
        selectedSkill.upgrade()
        update()
        showSkillDetails(Option(selectedSkill))
      } else {
        val alert = new Alert(AlertType.Error) {
          initOwner(dialogStage)
          title = "Insufficient Coins"
          headerText = "Not enough coins to upgrade skill."
          contentText = s"You need ${selectedSkill.cost - _playerCharacter.coins} more coins to level up."
        }
        alert.showAndWait()
      }
    }else{
      new Alert(AlertType.Error){
        initOwner(dialogStage)
        title       = "No Selection"
        headerText  = "No Skill Selected"
        contentText = "Please select a skill in the table."
      }.showAndWait()
    }
  }

  def equipClicked(action: ActionEvent): Unit = {
    val selectedIndex = skillTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      val selectedSkill = skillTable.selectionModel().selectedItem.value
      _playerCharacter.skill = selectedSkill
      if (currentSkill != _playerCharacter.skill) {
        skillChanged = true
      }
      update()
      showSkillDetails(Option(selectedSkill))
    }else{
      new Alert(AlertType.Error){
        initOwner(dialogStage)
        title       = "No Selection"
        headerText  = "No Skill Selected"
        contentText = "Please select a skill in the table."
      }.showAndWait()
    }
  }

  def exitClicked(action: ActionEvent): Unit = {
    dialogStage.close()
  }

}