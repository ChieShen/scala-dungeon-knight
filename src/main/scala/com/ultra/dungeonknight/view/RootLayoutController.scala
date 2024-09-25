package com.ultra.dungeonknight.view

import com.ultra.dungeonknight.MainApp
import com.ultra.dungeonknight.MainApp.stage
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Alert, ButtonType}
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}
import scalafxml.core.macros.sfxml

@sfxml
class RootLayoutController(){
  //Code from https://www.scalafx.org/docs/dialogs_and_alerts/
  def quitClicked(action: ActionEvent): Unit = {
    val alert = new Alert(Alert.AlertType.Confirmation) {
      initOwner(MainApp.stage)
      title = "Are you sure?"
      headerText = "Do you want to close the application?"
      contentText = "Your highscore will not be saved this way. Die in game to save your highscore."
    }

    val result = alert.showAndWait()
    result match {
      case Some(ButtonType.OK) =>
        // Close the application
        MainApp.stage.close()
      case _ =>
      // Do nothing (user selected Cancel or closed the dialog)
    }
  }

  def guideClicked(action: ActionEvent): Unit = {
    MainApp.showGuide()
  }
}