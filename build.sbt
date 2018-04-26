
name := "s-blockchain"

version := "0.1"

scalaVersion := "2.12.5"

lazy val src = (project in file("app"))
	.settings(
		libraryDependencies ++= Seq(
			"io.netty" % "netty-all" % "4.1.12.Final" withSources() withJavadoc(),
			"org.json4s" %% "json4s-native" % "3.5.0" withSources() withJavadoc(),
			// http://www.scalatest.org/install
			"org.scalactic" %% "scalactic" % "3.0.1" % "test",
			"org.scalatest" %% "scalatest" % "3.0.1" % "test"
		),
		mainClass in Compile := Some("com.sungjk.sblockchain.Main")
	)
