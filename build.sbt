name := "book-order-play-api"

version := "1.0.0"

scalaVersion := "2.13.14"

lazy val root = (project in file("."))
	.enablePlugins(PlayScala)
	.settings(
		PlayKeys.devSettings ++= Seq(
			"play.server.http.port" -> "9002",
			"http.port" -> "9002"
		),
		Compile / run / javaOptions ++= Seq(
			"-Dplay.server.http.port=9002",
			"-Dhttp.port=9002"
		),
		libraryDependencies ++= Seq(
			guice,
			jdbc,
			evolutions,
			"com.mysql" % "mysql-connector-j" % "8.4.0",
			"org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test
		)
	)