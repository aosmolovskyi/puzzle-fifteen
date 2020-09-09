name := "puzzle-fifteen"

version := "1.0"

scalaVersion := "2.13.1"

lazy val akkaVersion = "2.6.8"

libraryDependencies ++= Seq(
  "ch.qos.logback"    % "logback-classic"           % "1.2.3",
  "org.scalafx"       %% "scalafx"                  % "14-R19",
  "org.typelevel"     %% "cats-core"                % "2.2.0",
  "org.typelevel"     %% "cats-effect"              % "2.2.0",
  "org.specs2"        %% "specs2-core"              % "4.10.3"    % Test,
)

lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m =>
  "org.openjfx" % s"javafx-$m" % "14.0.1" classifier "mac"
)