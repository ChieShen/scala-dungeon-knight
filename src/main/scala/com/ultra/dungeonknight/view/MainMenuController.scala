package com.ultra.dungeonknight.view

import com.ultra.dungeonknight.MainApp
import com.ultra.dungeonknight.model.PlayerCharacter
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml

@sfxml
class MainMenuController(){
  def play(action : ActionEvent): Unit = {
    val playerCharacter = new PlayerCharacter("")
    if (MainApp.showEnterPlayerName(playerCharacter)){
      MainApp.showGameplayView(playerCharacter)
    }
  }

  def guideClicked(action: ActionEvent): Unit = {
    MainApp.showGuide()
  }

  def showHighscoreClicked(action: ActionEvent): Unit = {
    MainApp.showHighscoreView()
  }
}