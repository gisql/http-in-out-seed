val akkaHttpVersion = "10.0.9"

lazy val seed = (project in file("."))
  .settings(
    name := "adtest",
    version := "1.0",
    scalaVersion := "2.12.2",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-slf4j" % "2.4.19",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    )
  )

assemblyJarName in assembly := "adtest.jar"