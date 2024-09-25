package com.ultra.dungeonknight

import com.ultra.dungeonknight.model.PlayerCharacter
import com.ultra.dungeonknight.util.Database
import com.ultra.dungeonknight.view.{CharacterInfoDialogController, EnterNameDialogController, GameplayController, GuideDialogController, HighscoreViewController, ItemShopDialogController, SkillShopDialogController}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.scene.media.{Media, MediaPlayer, MediaView}
import scalafx.stage.{Modality, Stage}
import scalafx.util.Duration

object MainApp extends JFXApp {
  Database.setupDB()

  val playerList = new ObservableBuffer[PlayerCharacter]()

  playerList ++= PlayerCharacter.getAllPlayers

  // transform path of RootLayout.fxml to URI for resource location.
  val rootResource = getClass.getResource("view/RootLayout.fxml")
  // initialize the loader object.
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  // Load root layout from fxml file.
  loader.load();
  // retrieve the root component BorderPane from the FXML
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  // initialize stage

  //Add mp3 code from https://stackoverflow.com/questions/55527399/how-to-add-a-mp3-in-scalafx-gui-scene
  private val bgmPath = getClass.getResource("/music/TheFourRings.mp3").toString
  private val bgmMedia = new Media(bgmPath)
  private val bgm = new MediaPlayer(bgmMedia)

  stage = new PrimaryStage {
    title = "Dungeon Knight"
    icons += new Image(getClass.getResourceAsStream("/images/icon.png"))
    scene = new Scene {
      stylesheets= List(getClass.getResource("view/DungeonTheme.css").toString)
      root = roots
    }
    bgm.play()
    //code to loop from https://stackoverflow.com/questions/23498376/ahow-to-make-a-mp3-repeat-in-javafx
    bgm.setOnEndOfMedia(() => bgm.seek(Duration.Zero))
  }

  def showMainMenu() = {
    val resource = getClass.getResource("view/MainMenu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
    bgm.setVolume(1)
  }

  showMainMenu()

  def showEnterPlayerName(playerCharacter: PlayerCharacter): Boolean = {
    val resource = getClass.getResourceAsStream("view/EnterNameDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[EnterNameDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "Enter Name"
      scene = new Scene {
        stylesheets= List(getClass.getResource("view/DungeonTheme.css").toString)
        root = roots2
      }
      icons += new Image(getClass.getResourceAsStream("/images/icon.png"))
    }
    control.dialogStage = dialog
    control.playerCharacter = playerCharacter
    dialog.showAndWait()
    control.startClicked
  }

  def showGameplayView(playerCharacter: PlayerCharacter): Unit = {
    val resource = getClass.getResourceAsStream("view/GameplayView.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
    val control = loader.getController[GameplayController#Controller]
    control.playerCharacter = playerCharacter
    control.startGame()
    bgm.setVolume(0.05)
  }

  def showCharacterInfoDialog(playerCharacter: PlayerCharacter): PlayerCharacter = {
    val resource = getClass.getResourceAsStream("view/CharacterInfoDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots3 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[CharacterInfoDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "Character Info"
      scene = new Scene {
        stylesheets= List(getClass.getResource("view/DungeonTheme.css").toString)
        root = roots3
      }
      icons += new Image(getClass.getResourceAsStream("/images/icon.png"))
    }
    control.dialogStage = dialog
    control.playerCharacter = playerCharacter
    dialog.showAndWait()
    control.playerCharacter
  }

  def showSkillShopDialog(playerCharacter: PlayerCharacter): (PlayerCharacter,Boolean) ={
    val resource = getClass.getResourceAsStream("view/SkillShopDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots4 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[SkillShopDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "Skill Shop"
      scene = new Scene {
        stylesheets= List(getClass.getResource("view/DungeonTheme.css").toString)
        root = roots4
      }
      icons += new Image(getClass.getResourceAsStream("/images/icon.png"))
    }
    control.dialogStage = dialog
    control.playerCharacter = playerCharacter
    dialog.showAndWait()
    (control.playerCharacter,control.skillChanged)
  }

  def showItemShopDialog(playerCharacter: PlayerCharacter): PlayerCharacter ={
    val resource = getClass.getResourceAsStream("view/ItemShopDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots5 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[ItemShopDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "Item Shop"
      scene = new Scene {
        stylesheets= List(getClass.getResource("view/DungeonTheme.css").toString)
        root = roots5
      }
      icons += new Image(getClass.getResourceAsStream("/images/icon.png"))
    }
    control.dialogStage = dialog
    control.playerCharacter = playerCharacter
    dialog.showAndWait()
    control.playerCharacter
  }

  def showGuide(): Unit = {
    val resource = getClass.getResourceAsStream("view/GuideDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots5 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[GuideDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "Guide"
      scene = new Scene {
        stylesheets= List(getClass.getResource("view/DungeonTheme.css").toString)
        root = roots5
      }
      icons += new Image(getClass.getResourceAsStream("/images/icon.png"))
    }
    control.dialogStage = dialog
    dialog.showAndWait()
  }

  def showHighscoreView(): Unit = {
    val resource = getClass.getResourceAsStream("view/HighscoreView.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

}
