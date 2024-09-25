package com.ultra.dungeonknight.view

import com.ultra.dungeonknight.MainApp
import com.ultra.dungeonknight.model.PlayerCharacter
import org.omg.CORBA.ObjectHelper
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.event.ActionEvent
import scalafx.scene.control.{TableColumn, TableView}
import scalafxml.core.macros.sfxml

@sfxml
class HighscoreViewController(
                             private val playerTable: TableView[PlayerCharacter],
                             private val playerNameColumn: TableColumn[PlayerCharacter, String],
                             private val playerLevelColumn: TableColumn[PlayerCharacter,Int],
                             private val waveReachedColumn: TableColumn[PlayerCharacter, Int],
                             private val weaponNameColumn: TableColumn[PlayerCharacter, String],
                             private val highestDamageColumn: TableColumn[PlayerCharacter, Double]
                             ){
  playerTable.items = MainApp.playerList
  playerNameColumn.cellValueFactory = {
    x => new StringProperty(x.value.name)
  }
  playerLevelColumn.cellValueFactory = {
    x => ObjectProperty[Int](x.value.level)
  }
  waveReachedColumn.cellValueFactory = {
    x => ObjectProperty[Int](x.value.waveReached)
  }
  weaponNameColumn.cellValueFactory = {
    x => new StringProperty(x.value.weapon.name)
  }
  highestDamageColumn.cellValueFactory = {
    x => ObjectProperty[Double](x.value.highestDamage)
  }

  def mainMenuClicked(action : ActionEvent): Unit = {
    MainApp.showMainMenu()
  }
}
