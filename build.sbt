ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.19"

lazy val root = (project in file("."))
  .settings(
    name := "DungeonKnightTest3",
    libraryDependencies ++= Seq(
      // https://mvnrepository.com/artifact/org.scalafx/scalafx
      "org.scalafx" %% "scalafx" % "8.0.192-R14",
      // https://mvnrepository.com/artifact/org.scalafx/scalafxml-core-sfx8
      "org.scalafx" %% "scalafxml-core-sfx8" % "0.5",
      //https://scalikejdbc.org/
      "org.scalikejdbc" %% "scalikejdbc"       % "4.3.0",
      "com.h2database"  %  "h2"                % "2.2.224",

      // https://mvnrepository.com/artifact/org.apache.derby/derby
      "org.apache.derby" % "derby" % "10.12.1.1"
    )
  )
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
