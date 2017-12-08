import scala.util.Try

lazy val commonSettings = Seq[Def.SettingsDefinition](
  version := "0.6-SNAPSHOT",
  scalaVersion := "2.11.8",
  scapegoatVersion := "1.3.0",
  // @formatter:off
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Ywarn-unused-import"
  ),
  // @formatter:on
  libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest"                   % "3.0.1" % Test,
    "org.scalamock" %%% "scalamock-scalatest-support" % "3.5.0" % Test
  ),
  // the following check keeps throwing warnings in console mode
  scalacOptions in (Compile, console) ~= (_ filterNot (_ == "-Ywarn-unused-import")),
  // the following check gives false positives on mock definitions
  scalacOptions in (Test, compile) ~= (_ filterNot (_ == "-Ywarn-dead-code"))
)

lazy val ReleaseCmd = Command.command("release") { state =>
  "set elideOptions in client := Seq(\"-Xelide-below\", \"OFF\")" ::
    "client/clean" ::
    "backend/clean" ::
    "client/fullOptJS::webpack" ::
    "backend/test" ::
    "client/test" ::
    "backend/universal:packageZipTarball" ::
    "set elideOptions in client := Seq()" ::
    state
}

lazy val singlePageApp = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    commands += ReleaseCmd
  )
  .aggregate(backend, client, sharedJVM, sharedJS)

val gitDescribeWithoutHash = TaskKey[Option[String]]("git-describe")
gitDescribeWithoutHash in Global := {
  val command = Process("git describe --tags --first-parent")
  Try {
    command.lines.head.split("-g").head
  }.toOption
}

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "shared",
    libraryDependencies ++= Seq(
      "me.chrons"  %%% "boopickle"   % "1.2.5",
      "org.scalaz" %%% "scalaz-core" % scalazVersion
    ),
    buildInfoPackage := "shared",
    buildInfoObject := "BuildInfo",
    buildInfoKeys ++= Seq[BuildInfoKey](
      BuildInfoKey.map(BuildInfoKey.task(gitDescribeWithoutHash)) { case (_, v) => "gitDescribe" -> v }
    ),
    buildInfoOptions += BuildInfoOption.ToMap
  )

lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")

lazy val sharedJS = shared.js.settings(name := "sharedJS")

lazy val backend = (project in file("backend"))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "backend",
    //a lot of false positives in routes file
    scalacOptions ~= (_ filterNot (_ == "-Ywarn-unused-import")),
    PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value
  )
  .dependsOn(sharedJVM)

val scalazVersion       = "7.2.9"
val scalajsReactVersion = "0.11.3"
val scalaCssVersion     = "0.5.1"
val diodeVersion        = "1.1.0"
val reactVersion        = "15.3.2"
val clientBuildOutput   = file("./client/public/app/")
val monocleVersion      = "1.4.0"
val uikitVersion        = "3.0.0-beta.24"

lazy val elideOptions = settingKey[Seq[String]]("Set limit for elidable functions")

lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "client",
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js"                      %%% "scalajs-dom"   % "0.9.1",
      "com.github.japgolly.scalajs-react" %%% "core"          % scalajsReactVersion,
      "com.github.japgolly.scalajs-react" %%% "extra"         % scalajsReactVersion,
      "com.github.japgolly.scalacss"      %%% "core"          % scalaCssVersion,
      "com.github.japgolly.scalacss"      %%% "ext-react"     % scalaCssVersion,
      "me.chrons"                         %%% "diode-react"   % diodeVersion,
      "com.github.julien-truffaut"        %%% "monocle-core"  % monocleVersion,
      "com.github.julien-truffaut"        %%% "monocle-macro" % monocleVersion
    ),
    addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full),
    elideOptions := Seq(),
    scalacOptions ++= elideOptions.value,
    scalaJSStage in Global := FullOptStage,
    crossTarget in (Compile, fastOptJS) := clientBuildOutput,
    crossTarget in (Compile, fullOptJS) := clientBuildOutput,
    npmDependencies in Compile ++= Seq(
      "react" -> reactVersion,
      "react-dom" -> reactVersion,
      "uikit" -> uikitVersion,
      "expose-loader" -> "0.7.1"
    ),
    webpackConfigFile := Some(baseDirectory.value / "webpack.config.js"),
    scalaJSStage in Test := FastOptStage,
    skip in packageJSDependencies := false
  )
  .dependsOn(sharedJS)
