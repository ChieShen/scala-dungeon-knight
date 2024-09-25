package com.ultra.dungeonknight.util

import com.ultra.dungeonknight.model.{Entity, Mob, PlayerCharacter}
import scalafx.animation._
import scalafx.beans.property.DoubleProperty
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration

class Animation(){

  def playIdleAnimation(user: Entity, imageView: ImageView): Timeline = {
    var imageIndex = 0
    val frameTime = Duration(80)
    val imageArray = user.idleImageList

    //idea from https://stackoverflow.com/questions/47876381/javafx-and-sprite-animation-how-do-i-make-an-animation-cycle-to-change-pictures
    //Keyframe code https://docs.oracle.com/javase/8/javafx/api/javafx/animation/KeyFrame.html
    val timeline = new Timeline {
      cycleCount = Timeline.Indefinite
      keyFrames = Seq(
        KeyFrame(frameTime, onFinished = _ => {
          imageView.image = imageArray(imageIndex)
          imageIndex = (imageIndex + 1) % imageArray.size
        })
      )
    }
    timeline
  }

  def playAttackAnimation(user: Entity, target: Entity, userImageView: ImageView
                          , targetImageView: ImageView, originalWidth: Double,
                          targetHealthBar: Rectangle) : SequentialTransition = {
    val walkingFrames = user.walkingImageList
    val attackFrames = user.attackImageList

    var animationIndex = 0
    val walkingTime = Duration(500)
    val attackTime = Duration(400)

    val walkingTimeline = new Timeline {
      cycleCount = 1
      keyFrames = Seq(
        KeyFrame(walkingTime, onFinished = _ => {
          userImageView.image = walkingFrames(animationIndex)
          animationIndex = (animationIndex + 1) % walkingFrames.size
        })
      )
    }

    val attackTimeline = new Timeline {
      cycleCount = 1
      keyFrames = Seq(
        KeyFrame(attackTime, onFinished = _ => {
          userImageView.image = attackFrames(animationIndex)
          animationIndex = (animationIndex + 1) % attackFrames.size
        })
      )
    }

    val walkToEnemyTransition = new TranslateTransition(walkingTime, userImageView) {
      if (user.isInstanceOf[Mob]){
        toX = (targetImageView.layoutX - userImageView.layoutX + 50).toDouble
      }else {
        toX = (targetImageView.layoutX - userImageView.layoutX - 50).toDouble
      }
      toY = (targetImageView.layoutY - userImageView.layoutY).toDouble
    }

    val walkBackTransition = new TranslateTransition(walkingTime, userImageView) {
      toX = 0
      toY = 0
    }

    val attackTransition = new PauseTransition(attackTime)

    val targetHealthBarTimeline = new Timeline {
      cycleCount = 1
      keyFrames = Seq(
        KeyFrame(attackTime, onFinished = _ => {
          val newHealth = target.health
          val maxHealth = target.maxHealth
          val newWidth = (newHealth / maxHealth) * originalWidth

          targetHealthBar.width <== DoubleProperty(newWidth)
        })
      )
    }

    val fullAnimation = new SequentialTransition(
      children = Seq(
        walkingTimeline,
        walkToEnemyTransition,
        attackTimeline,
        attackTransition,
        targetHealthBarTimeline,
        walkBackTransition,
      )
    )

    fullAnimation
  }
}
