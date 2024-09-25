package com.ultra.dungeonknight.view

import com.ultra.dungeonknight.model.PlayerCharacter
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, TextField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class EnterNameDialogController(
                               private val nameTextField: TextField,
                               ){
  var dialogStage : Stage = null
  private var _playerCharacter: PlayerCharacter = null
  var startClicked: Boolean = false

  def playerCharacter = _playerCharacter
  def playerCharacter_=(playerCharacter: PlayerCharacter){
    _playerCharacter = playerCharacter
    nameTextField.text.value = _playerCharacter.name
  }

  def startSelected(action: ActionEvent): Unit ={
    if (validInput()){
      _playerCharacter.name = nameTextField.text.value
      startClicked = true
      dialogStage.close()
    }
  }

  def nullInput(name: String): Boolean = {
    name == null || name.length == 0
  }

  def validInput(): Boolean = {
    var errorMessage = ""

    if (nullInput(nameTextField.text.value)){
      errorMessage += "Player name cannot be empty\n"
    }

    if (errorMessage.length() == 0){
      true
    }
    else{
      val alert = new Alert(Alert.AlertType.Error){
        initOwner(dialogStage)
        title = "Error Message"
        headerText = "Please provide a player name to start the game"
        contentText = errorMessage
      }.showAndWait()

      false
    }
  }

  def backClicked(action: ActionEvent): Unit = {
    dialogStage.close()
  }
}