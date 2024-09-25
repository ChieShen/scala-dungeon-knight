package com.ultra.dungeonknight.view

import scalafx.event.ActionEvent
import scalafx.scene.control.TextArea
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class GuideDialogController(
                         private val guideTextArea: TextArea
                         ){
  var dialogStage: Stage = null
  private val guide =
    """|1. Star the game by pressing the Play button.
      |2. Enter a name start the game.
      |3. There are two types of attacks: Basic and Skill. Skills have cooldowns while basic attack do not.
      |4. Level up your character, skills, and buy new equipment by pressing the buttons on the top left.
      |5. Level up restore your character's health back to max so save up some coins for battling bosses.
      |6. Bosses occur every 5th round. Goblin drops more coins compared to Golems.
      |7. If you wish to quit, use the menu button to quit.
      |8. There game does not end unless you die or quit, go as far as you can!
      |""".stripMargin
  guideTextArea.text = guide

  def exitClicked(action: ActionEvent): Unit = {
    dialogStage.close()
  }
}