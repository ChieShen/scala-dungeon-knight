package com.ultra.dungeonknight.model

abstract class Weapon{
  var name = ""
  var cost = 0
  var strength: Double = 0.0
  var defensePenetration: Double = 0.0
  var description = ""
  var owned = false
}

class StarterSword extends Weapon{
  name = "Starter Sword"
  strength = 10
  defensePenetration = 5
  description = s"Simple sword that adds $strength strength and $defensePenetration defense penetration."
}

class TruthSeeker extends Weapon{
  name = "Truth Seeker"
  strength = 30
  defensePenetration = 50
  cost = 1000
  description = s"A sword that reveals your opponent's weakness. Adds $strength strength and $defensePenetration defense penetration"
}

class Oblivion extends Weapon{
  name = "Oblivion"
  strength = 100
  cost = 2000
  description = s"A destructive sword that adds $strength strength"
}